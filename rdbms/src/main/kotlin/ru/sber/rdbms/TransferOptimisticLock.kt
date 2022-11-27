package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock(private val connection: Connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val accountVersion1 = getAccountVersion(connection, accountId1)
        val accountVersion2 = getAccountVersion(connection, accountId2)
        doAccountTransaction(connection, accountId1, accountVersion1, -amount)
        doAccountTransaction(connection, accountId2, accountVersion2, amount)
    }

    /**
     * Выполняем транзакцию по счёту
     */
    private fun doAccountTransaction(
        connection: Connection,
        accountId: Long,
        version: Int,
        amount: Long
    ) {
        val preparedStatement = connection.prepareStatement(
            "update account set amount = amount + ?, version = version + 1 where id = ? and version = ?"
        )

        preparedStatement.use {
            it.setLong(1, amount)
            it.setLong(2, accountId)
            it.setInt(3, version)

            val result = it.executeUpdate()
            println("result = $result")

            if (result == 0) {
                throw SQLException("Concurrent update")
            }
        }
    }

    /**
     * Получаем версию счёта
     */
    private fun getAccountVersion(connection: Connection, accountId: Long): Int {
        val preparedStatement = connection.prepareStatement(
            "select * from account where id = ?"
        )

        preparedStatement.use {
            it.setLong(1, accountId)
            it.executeQuery().use { rs ->
                rs.next()
                return rs.getInt("version")
            }
        }
    }
}
