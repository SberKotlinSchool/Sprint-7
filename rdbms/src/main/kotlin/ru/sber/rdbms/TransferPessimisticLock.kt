package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(
            CONNECTION_PROPERTIES["url"],
            CONNECTION_PROPERTIES["username"],
            CONNECTION_PROPERTIES["password"]
        ).use { conn ->
            conn.autoCommit = false
            runCatching {
                changeBalance(conn, accountId1, -amount)
                changeBalance(conn, accountId2, amount)
                conn.commit()
            }.onFailure {
                it.printStackTrace()
                conn.rollback()
            }.also {
                conn.autoCommit = true
            }
        }
    }

    private fun changeBalance(connection: Connection, accountId: Long, amount: Long) {
        connection.prepareStatement(SELECT_URL).use {
            it.setLong(1, accountId)
            it.executeQuery().run {
                next()
                if (getLong("amount") + amount < 0) {
                    throw SQLException("В результате транзакции баланс на счету $accountId станет меньше нуля")
                }
            }
        }

        connection.prepareStatement(UPDATE_RUL).use {
            it.setLong(1, amount)
            it.setLong(2, accountId)
            it.executeUpdate()
        }
    }

    companion object {
        private const val SELECT_URL = "select * from account1 where id=? for update"
        private const val UPDATE_RUL = "update account1 set amount=amount+? where id=?"
        private val CONNECTION_PROPERTIES = mapOf(
            "url" to "jdbc:postgresql://localhost:5432/db",
            "username" to "postgres",
            "password" to "postgres"
        )
    }
}
