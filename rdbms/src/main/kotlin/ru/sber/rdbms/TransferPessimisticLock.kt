package ru.sber.rdbms

import java.sql.Connection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.sber.rdbms.DbUtils.Companion.UPDATE_QUERY
import ru.sber.rdbms.exceptions.ConcurrentUpdateException
import ru.sber.rdbms.exceptions.TransferExecutionException

suspend fun main() {
  val transferPessimisticLock = TransferPessimisticLock()
  coroutineScope {
    for (i in 1L..10L) {
      launch { transferPessimisticLock.transfer(1, 2, i) }
      launch { transferPessimisticLock.transfer(2, 1, i * 2) }
    }
  }
}

class TransferPessimisticLock {

  fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
    DbUtils().connection.use { connection ->
      val autoCommit = connection.autoCommit
      connection.autoCommit = false

      runCatching {
        val accounts = DbUtils.getAccountsForUpdate(connection, listOf(accountId1, accountId2))

        accounts.find { it?.id == accountId1 }?.let { account ->
          if (account.amount - amount < 0)
            throw TransferExecutionException("Недостаточно средств для перевода")
          else {
            update(connection, account.id, -amount)
          }
        } ?: throw TransferExecutionException("Счет не найден")

        accounts.find { it?.id == accountId2 }?.let { account ->
          update(connection, account.id, amount)
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

  private fun update(connection: Connection, id: Long, amount: Long) {
    connection.prepareStatement(UPDATE_QUERY).use {
      it.setLong(1, amount)
      it.setLong(2, id)
      val result = it.executeUpdate()
      if (result == 0)
        throw ConcurrentUpdateException("Не удалось выполнить операцию")
    }
  }
}