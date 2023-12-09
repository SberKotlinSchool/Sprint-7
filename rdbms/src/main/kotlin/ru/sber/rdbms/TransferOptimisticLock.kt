package ru.sber.rdbms

import java.sql.Connection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.sber.rdbms.DbUtils.Companion.UPDATE_BY_VERSION_QUERY
import ru.sber.rdbms.exceptions.ConcurrentUpdateException
import ru.sber.rdbms.exceptions.TransferExecutionException

suspend fun main() {
  val transferOptimisticLock = TransferOptimisticLock()
  coroutineScope {
    for (i in 1L..10L) {
      launch { transferOptimisticLock.transfer(1, 2, i) }
    }
  }
}

class TransferOptimisticLock {

  fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
    DbUtils().connection.use { connection ->
      val autoCommit = connection.autoCommit
      connection.autoCommit = false

      runCatching {
        val account1 = DbUtils.getAccount(connection, accountId1)

        account1?.let { account ->
          if (account.amount - amount < 0)
            throw TransferExecutionException("Недостаточно средств для перевода")
          else {
            update(connection, account.id, -amount, account.version)
          }
        } ?: throw TransferExecutionException("Счет не найден")

        val account2 = DbUtils.getAccount(connection, accountId2)

        account2?.let { account ->
          update(connection, account.id, amount, account.version)
        } ?: throw TransferExecutionException("Счет не найден")

        connection.commit()
      }.onFailure {
        connection.rollback()
        if (it is ConcurrentUpdateException) {
          println("retry transfer...")
          transfer(accountId1, accountId2, amount)
        } else
          throw it
      }.also { connection.autoCommit = autoCommit }
    }
  }

  private fun update(connection: Connection, id: Long, amount: Long, version: Int) {
    connection.prepareStatement(UPDATE_BY_VERSION_QUERY).use {
      it.setLong(1, amount)
      it.setInt(2, version + 1)
      it.setLong(3, id)
      it.setInt(4, version)
      val result = it.executeUpdate()
      if (result == 0)
        throw ConcurrentUpdateException("Не удалось выполнить операцию")
    }
  }
}
