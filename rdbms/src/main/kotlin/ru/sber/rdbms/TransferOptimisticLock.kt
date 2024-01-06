package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(
            CONNECTION_PROPERTIES["url"],
            CONNECTION_PROPERTIES["username"],
            CONNECTION_PROPERTIES["password"]
        ).use { conn ->
            runCatching {
                conn.autoCommit = false
                changeBalance(conn, accountId1, -amount)
                changeBalance(conn, accountId2, amount)
                conn.commit()
            }.onFailure {
                it.printStackTrace()
                conn.rollback()
            }.also {
                conn.autoCommit = true
            }.getOrNull()
        }
    }

    private fun changeBalance(connection: Connection, accountId: Long, amount: Long) {
        var accountAmount: Long
        var version: Long

        connection.prepareStatement(SELECT_URL).use {
            it.setLong(1, accountId)
            it.executeQuery().use { resultSet ->
                resultSet.next()
                accountAmount = resultSet.getLong("amount")
                version = resultSet.getLong("version")
            }
        }

        if (accountAmount + amount < 0) {
            throw SQLException("В результате транзакции баланс на счету $accountId станет меньше нуля")
        }

        connection.prepareStatement(UPDATE_RUL).use {
            it.setLong(1, accountAmount + amount)
            it.setLong(2, accountId)
            it.setLong(3, version)
            val changedRowsNum = it.executeUpdate()
            if (changedRowsNum == 0) {
                throw SQLException("Concurrent update on accountId=$accountId")
            }
        }
    }

    companion object {
        private const val SELECT_URL = "select amount, version from account1 where id=?"
        private const val UPDATE_RUL = "update account1 set amount=?, version=version+1 where id=? and version=?"
        private val CONNECTION_PROPERTIES = mapOf(
            "url" to "jdbc:postgresql://localhost:5432/db",
            "username" to "postgres",
            "password" to "postgres"
        )
    }
}
