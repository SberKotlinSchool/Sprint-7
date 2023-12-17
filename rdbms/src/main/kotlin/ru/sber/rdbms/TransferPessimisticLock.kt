package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private const val BLOCK_SQL = """
            select * 
            from account 
            where id = ? 
            for update
        """

private const val UPDATE_SQL = """
            update account 
            set amount = amount + ? 
            where id = ?
        """

class TransferPessimisticLock {
    fun updateAccountAmount(accountId1: Long, accountId2: Long, amount: Long) {
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
                    blockAccountAndValidate(conn, accountId1, -amount)
                    blockAccountAndValidate(conn, accountId2, amount)
                } else {
                    blockAccountAndValidate(conn, accountId2, amount)
                    blockAccountAndValidate(conn, accountId1, -amount)
                }

                updateAccountAmount(conn, -amount, accountId1)
                updateAccountAmount(conn, amount, accountId2)

                conn.commit()
                println("transfer $amount from $accountId1 to $accountId2")
            } catch (exception: TransferExceptions) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun blockAccountAndValidate(conn: Connection, accountId: Long, amount: Long) {
        val prepareStatement1 = conn.prepareStatement(BLOCK_SQL.trimIndent())
        prepareStatement1.use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()
                if (amount < 0 && it.getLong("amount") + amount < 0) {
                    throw TransferExceptions("not enough funds $amount in the account $accountId")
                }
            }
        }
    }

    private fun updateAccountAmount(conn: Connection, amount: Long, accountId: Long) {
        val prepareStatement2 = conn.prepareStatement(UPDATE_SQL.trimIndent())
        prepareStatement2.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.executeUpdate()
        }
    }
}

fun main() {
    TransferPessimisticLock().updateAccountAmount(1, 2, 10000)
    TransferPessimisticLock().updateAccountAmount(2, 1, 200)
}

