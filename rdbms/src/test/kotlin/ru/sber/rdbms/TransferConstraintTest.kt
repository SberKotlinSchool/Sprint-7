package ru.sber.rdbms

import java.sql.SQLException
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TransferConstraintTest {

  private val transferConstraint = TransferConstraint()

  @BeforeEach
  fun init() {
    DbUtils().connection.use {
      val autoCommit = it.autoCommit
      it.autoCommit = false
      it.prepareStatement("DELETE FROM account1").executeUpdate()
      it.prepareStatement("INSERT INTO account1 VALUES (1,20,1),(2,0,1)").executeUpdate()
      it.commit()
      it.autoCommit = autoCommit
    }
  }

  @Test
  fun transferSuccess() {
    transferConstraint.transfer(1, 2, 10)
    assertEquals(10, DbUtils.getAccount(DbUtils().connection, 1)!!.amount)
    assertEquals(10, DbUtils.getAccount(DbUtils().connection, 2)!!.amount)
  }

  @Test
  fun transferFailure() {
    assertThrows<SQLException> { transferConstraint.transfer(1, 2, 40) }
    assertEquals(20, DbUtils.getAccount(DbUtils().connection, 1)!!.amount)
    assertEquals(0, DbUtils.getAccount(DbUtils().connection, 2)!!.amount)
  }
}