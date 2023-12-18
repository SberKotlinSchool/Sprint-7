package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException
import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock {
    companion object {
        const val SELECT_ACCOUNT_QUERY = "SELECT * FROM account1 WHERE id = ?"
        const val UPDATE_ACCOUNT_QUERY =
            "UPDATE account1 SET amount = amount + ?, version = version + 1 WHERE id = ? AND version = ?"
    }

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

                val versionFrom: Int
                val versionTo: Int

                val selectFromStatement = conn.prepareStatement(SELECT_ACCOUNT_QUERY)
                selectFromStatement.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        versionFrom = it.getInt("version")
                        val amountBefore = it.getLong("amount")
                        if (amountBefore < amount)
                            throw SQLException("Insufficient funds for account")
                    }
                }
                val selectToStatement = conn.prepareStatement(SELECT_ACCOUNT_QUERY)
                selectToStatement.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery().use {
                        it.next()
                        versionTo = it.getInt("version")
                    }
                }

                val updateFromStatement = conn.prepareStatement(UPDATE_ACCOUNT_QUERY)
                updateFromStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.setInt(3, versionFrom)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw SQLException("Concurrent update detected for account: $accountId")
                }
                val updateSecondStatement = conn.prepareStatement(UPDATE_ACCOUNT_QUERY)
                updateSecondStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setInt(3, versionTo)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw SQLException("Concurrent update detected for account: $accountId")
                }
                conn.commit()

                println("Transfer from $accountId1 to $accountId2: $amount")
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}