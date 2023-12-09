package ru.sber.rdbms

import kotlin.test.assertEquals
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.sber.rdbms.exceptions.TransferExecutionException

class TransferOptimisticLockTest {

  private val transferOptimisticLock = TransferOptimisticLock()

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
          launch { transferOptimisticLock.transfer(1, 2, i) }
        }
      }
    }
    assertEquals(15, DbUtils.getAccount(DbUtils().connection, 1)!!.amount)
    assertEquals(55, DbUtils.getAccount(DbUtils().connection, 2)!!.amount)
  }

  @Test
  fun transferWithTransferExecutionException() {
    runBlocking {
      coroutineScope {
        for (i in 1L..70L) {
          launch { transferOptimisticLock.transfer(1, 2, 1) }
        }
      }
    }
    assertThrows<TransferExecutionException> { transferOptimisticLock.transfer(1, 2, 1) }
    assertEquals(0, DbUtils.getAccount(DbUtils().connection, 1)!!.amount)
    assertEquals(70, DbUtils.getAccount(DbUtils().connection, 2)!!.amount)
  }
}