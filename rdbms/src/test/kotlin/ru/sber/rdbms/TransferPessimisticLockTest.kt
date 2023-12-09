package ru.sber.rdbms

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.sber.rdbms.exceptions.TransferExecutionException

class TransferPessimisticLockTest {

  private val transferPessimisticLock = TransferPessimisticLock()

  @BeforeEach
  fun init() {
    DbUtils().connection.use {
      val autoCommit = it.autoCommit
      it.autoCommit = false
      it.prepareStatement("DELETE FROM account1").executeUpdate()
      it.prepareStatement("INSERT INTO account1 VALUES (1,70,1),(2,0,1)").executeUpdate()
      it.commit()
      it.autoCommit = autoCommit
    }
  }

  @Test
  fun transferSuccess() {
    runBlocking {
      coroutineScope {
        for (i in 1L..10L) {
          launch { transferPessimisticLock.transfer(1, 2, i) }
        }
      }
    }
    kotlin.test.assertEquals(15, DbUtils.getAccount(DbUtils().connection, 1)!!.amount)
    kotlin.test.assertEquals(55, DbUtils.getAccount(DbUtils().connection, 2)!!.amount)
  }

  @Test
  fun transferWithTransferExecutionException() {
    runBlocking {
      coroutineScope {
        for (i in 1L..10L) {
          launch { transferPessimisticLock.transfer(1, 2, i) }
          launch { transferPessimisticLock.transfer(2, 1, i) }
        }
      }
    }
    org.junit.jupiter.api.assertThrows<TransferExecutionException> { transferPessimisticLock.transfer(2, 1, 1) }
    kotlin.test.assertEquals(70, DbUtils.getAccount(DbUtils().connection, 1)!!.amount)
    kotlin.test.assertEquals(0, DbUtils.getAccount(DbUtils().connection, 2)!!.amount)
  }
}