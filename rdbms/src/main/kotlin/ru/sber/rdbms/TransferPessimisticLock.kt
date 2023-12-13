package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                if (accountId1 < accountId2) {
                    transferMoney(conn, accountId1, -amount)
                    transferMoney(conn, accountId2, amount)
                } else {
                    transferMoney(conn, accountId2, amount)
                    transferMoney(conn, accountId1, -amount)
                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun transferMoney(connection: Connection, accountId: Long, amount: Long) {
        var currentAmount: Long
        val statementSelectForUpdate = connection.prepareStatement("select * from account where id = ? for update")
        statementSelectForUpdate.use { preparedStatement ->
            preparedStatement.setLong(1, accountId)
            preparedStatement.executeQuery().use {
                it.next()
                currentAmount = it.getLong("amount")
            }
        }
        if (currentAmount + amount < 0) throw SQLException("Not enough money")

        val statementUpdate = connection.prepareStatement("update account set amount = ? where id = ?")
        statementUpdate.use { preparedStatement ->
            preparedStatement.setLong(1, currentAmount + amount)
            preparedStatement.setLong(2, accountId)

            preparedStatement.executeUpdate()
        }
    }
}

fun main() {
    TransferPessimisticLock().transfer(1, 2, 200)
}