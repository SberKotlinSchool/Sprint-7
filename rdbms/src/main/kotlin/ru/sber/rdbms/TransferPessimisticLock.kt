package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Connection

class TransferPessimisticLock(private val dataSource: DataSource) {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        var (accountIdFirst, accountIdSecond, resultAmount) = determineAccountOrder(accountId1, accountId2, amount)

        dataSource.connection.use { connection ->
            performTransfer(connection, accountIdFirst, accountIdSecond, resultAmount, amount)
        }
    }

    private fun determineAccountOrder(accountId1: Long, accountId2: Long, amount: Long): Triple<Long, Long, Long> {
        return if (accountId1 > accountId2) {
            Triple(accountId2, accountId1, -amount)
        } else {
            Triple(accountId1, accountId2, amount)
        }
    }

    private fun performTransfer(
        connection: Connection,
        accountIdFirst: Long,
        accountIdSecond: Long,
        resultAmount: Long,
        originalAmount: Long
    ) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                validateAccountBalance(conn, accountIdFirst, originalAmount)

                updateAccount(conn, accountIdFirst, -resultAmount)
                updateAccount(conn, accountIdSecond, resultAmount)

                conn.commit()

                println("Transfer from $accountIdFirst to $accountIdSecond: $originalAmount")
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun validateAccountBalance(conn: Connection, accountId: Long, amount: Long) {
        conn.prepareStatement("SELECT amount FROM account1 WHERE id = ? FOR UPDATE").use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                if (it.next()) {
                    val amountBefore = it.getLong("amount")
                    if (amountBefore < amount) {
                        throw SQLException("Not enough money")
                    }
                }
            }
        }
    }

    private fun updateAccount(conn: Connection, accountId: Long, amount: Long) {
        conn.prepareStatement("UPDATE account1 SET amount = amount + ?, version = version + 1 WHERE id = ?").use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.executeUpdate()
        }
    }

    interface DataSource {
        val connection: Connection
    }

    class DriverManagerDataSource(private val url: String, private val user: String, private val password: String) : DataSource {
        override val connection: Connection
            get() = DriverManager.getConnection(url, user, password)
    }
}

