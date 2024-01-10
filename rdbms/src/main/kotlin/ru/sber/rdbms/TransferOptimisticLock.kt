package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock(private val dataSource: DataSource) {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        dataSource.connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val (versionFrom, amountBefore) = getAccountDetails(conn, accountId1, "account1")
                if (amountBefore < amount)
                    throw SQLException("Not enough money")

                val versionTo = getAccountVersion(conn, accountId2, "account1")

                updateAccount(conn, accountId1, versionFrom, -amount, "account1")
                updateAccount(conn, accountId2, versionTo, amount, "account1")

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

    private fun getAccountDetails(conn: Connection, accountId: Long, tableName: String): Pair<Int, Long> {
        val selectStatement = conn.prepareStatement("SELECT version, amount FROM $tableName WHERE id = ?")
        selectStatement.use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                if (it.next()) {
                    val version = it.getInt("version")
                    val amount = it.getLong("amount")
                    return Pair(version, amount)
                } else {
                    throw SQLException("Account not found: $accountId")
                }
            }
        }
    }

    private fun getAccountVersion(conn: Connection, accountId: Long, tableName: String): Int {
        val selectStatement = conn.prepareStatement("SELECT version FROM $tableName WHERE id = ?")
        selectStatement.use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                if (it.next()) {
                    return it.getInt("version")
                } else {
                    throw SQLException("Account not found: $accountId")
                }
            }
        }
    }

    private fun updateAccount(conn: Connection, accountId: Long, version: Int, amount: Long, tableName: String) {
        val updateStatement = conn.prepareStatement("UPDATE $tableName SET amount = amount + ?, version = version + 1 WHERE id = ? AND version = ?")
        updateStatement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update: $accountId")
        }
    }
}

interface DataSource {
    val connection: Connection
}

class DriverManagerDataSource(private val url: String, private val user: String, private val password: String) : DataSource {
    override val connection: Connection
        get() = DriverManager.getConnection(url, user, password)
}
