package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    private val selectForUpdateQuery = "select * from account1 where id = ? for update"
    private val accountUpdateQuery = "update account1 set amount = ? where id = ?"
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/rdbms",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                if (accountId1 < accountId2) {
                    doOperation(conn, accountId1, -amount)
                    doOperation(conn, accountId2, amount)
                } else {
                    doOperation(conn, accountId2, amount)
                    doOperation(conn, accountId1, -amount)
                }

                conn.commit()
            } catch (exception: TransferException) {
                rollback(conn, exception)
            } catch (exception: SQLException) {
                rollback(conn, exception)
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun doOperation(connection: Connection, accountId: Long, amount: Long) {
        var currentAmount: Long
        connection.prepareStatement(selectForUpdateQuery).use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()
                currentAmount = it.getLong("amount")
            }
        }

        if (currentAmount + amount < 0)
            throw TransferException("Not enough money")

        connection.prepareStatement(accountUpdateQuery).use { statement ->
            statement.setLong(1, currentAmount + amount)
            statement.setLong(2, accountId)
            statement.executeUpdate()
        }
    }

    private fun rollback(connection: Connection, exception: Exception) {
        println(exception.message)
        exception.printStackTrace()
        connection.rollback()
    }
}
