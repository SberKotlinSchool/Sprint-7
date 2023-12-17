package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private const val UPDATE_SQL = """
            update account
            set amount  = amount + ?,
                version = version + 1
            where id = ?
              and version = ?;
      """

private const val SELECT_SQL = """
            select * 
            from account 
            where id = ?
        """

class TransferOptimisticLock {
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

                val version1 = getVersionAndValidate(conn, accountId1, -amount)
                val version2 = getVersionAndValidate(conn, accountId2, amount)

                updateAccountAmount(conn, -amount, accountId1, version1)
                updateAccountAmount(conn, amount, accountId2, version2)

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

    private fun updateAccountAmount(conn: Connection, amount: Long, accountId: Long, version: Int) {
        val prepareStatement =
            conn.prepareStatement(
                UPDATE_SQL.trimIndent()
            )
        prepareStatement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)

            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update")
        }
    }

    private fun getVersionAndValidate(conn: Connection, accountId: Long, amount: Long): Int {
        val prepareStatement1 = conn.prepareStatement(SELECT_SQL.trimIndent())

        prepareStatement1.use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()
                if (amount < 0 && it.getLong("amount") + amount < 0) {
                    throw TransferExceptions("not enough funds $amount in the account $accountId")
                }
                return it.getInt("version")
            }
        }
    }
}

fun main() {
    TransferOptimisticLock().transfer(1, 2, 1000)
    TransferOptimisticLock().transfer(2, 3, 100)
}
