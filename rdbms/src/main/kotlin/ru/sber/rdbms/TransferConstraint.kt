package ru.sber.rdbms

import ru.sber.rdbms.DbUtils.Companion.UPDATE_QUERY

fun main() {
  val transferConstraint = TransferConstraint()
  transferConstraint.transfer(1, 2, 10)
  transferConstraint.transfer(2, 1, 5)
}

class TransferConstraint {

  fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
    DbUtils().connection.use { connection ->
      val autoCommit = connection.autoCommit
      connection.autoCommit = false
      runCatching {
        connection.prepareStatement(UPDATE_QUERY).use { ps ->
          ps.setLong(1, -amount)
          ps.setLong(2, accountId1)
          ps.executeUpdate()
        }
        connection.prepareStatement(UPDATE_QUERY).use { ps ->
          ps.setLong(1, amount)
          ps.setLong(2, accountId2)
          ps.executeUpdate()
        }
        connection.commit()
      }.onFailure {
        connection.rollback()
        throw it
      }.also { connection.autoCommit = autoCommit }
    }
  }
}
