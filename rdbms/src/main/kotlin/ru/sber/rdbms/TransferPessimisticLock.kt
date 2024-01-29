package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    companion object {
        private const val account = "select * from account1 where id = ? for update"
        private const val update = "update account1 set amount = ? where id = ?"
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres", "postgres"
        )
    }

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                if (accountId1 < accountId2) {
                    send(conn, accountId1, -amount)
                    send(conn, accountId2, amount)
                } else {
                    send(conn, accountId2, amount)
                    send(conn, accountId1, -amount)
                }
                conn.commit()
            } catch (exception: Exception ) {
                rollback(conn, exception)
            } catch (exception: SQLException) {
                rollback(conn, exception)
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun send(connection: Connection, accountId: Long, amount: Long) {
        var currentAmount: Long
        connection.prepareStatement(account).use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()
                currentAmount = it.getLong("amount")
            }
        }
        if (currentAmount + amount < 0)
            throw Exception("Not enough money")

        connection.prepareStatement(update).use { statement ->
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
