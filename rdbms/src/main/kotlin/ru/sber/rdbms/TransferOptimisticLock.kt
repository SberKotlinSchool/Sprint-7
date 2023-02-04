package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    private val accountUpdateQuery =
        "update account1 set amount = ?, version = version + 1 where id = ? and version = ?"
    private val accountCurrentStateQuery = "select version, amount from account1 where id = ?"
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

                //Withdraw
                doOperation(conn, accountId1, -amount)

                //Deposit
                doOperation(conn, accountId2, amount)

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

    private fun rollback(connection: Connection, exception: Exception) {
        println(exception.message)
        exception.printStackTrace()
        connection.rollback()
    }

    private fun doOperation(connection: Connection, accountId: Long, amount: Long) {
        var version: Int
        var currentAmount: Long
        connection.prepareStatement(accountCurrentStateQuery).use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()
                version = it.getInt("version")
                currentAmount = it.getLong("amount")
            }
        }

        if (currentAmount + amount < 0)
            throw TransferException("Not enough money")

        connection.prepareStatement(accountUpdateQuery).use { statement ->
            statement.setLong(1, currentAmount + amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw TransferException("Concurrent update")
        }
    }
}
