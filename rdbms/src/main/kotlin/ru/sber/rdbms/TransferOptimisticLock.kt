package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {

    companion object {
        private const val update = "update account1 set amount = ?, version = version + 1 where id = ? and version = ?"
        private const val getCurrent = "select version, amount from account1 where id = ?"
        val connection: Connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres", "postgres"
        )
    }

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                send(conn, accountId1, -amount)
                send(conn, accountId2, amount)
                conn.commit()
            } catch (exception: Exception) {
                rollback(conn, exception)
            } catch (exception: SQLException) {
                rollback(conn, exception)
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun rollback(connection: Connection, exception: Exception) {
        println(exception.message)
        exception.printStackTrace()
        connection.rollback()
    }

    private fun send(connection: Connection, accountId: Long, amount: Long) {
        var version: Int
        var currentAmount: Long
        connection.prepareStatement(getCurrent).use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()
                version = it.getInt("version")
                currentAmount = it.getLong("amount")
            }
        }

        connection.prepareStatement(update).use { statement ->
            statement.setLong(1, currentAmount + amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw Exception("Concurrent update")
        }
    }
}