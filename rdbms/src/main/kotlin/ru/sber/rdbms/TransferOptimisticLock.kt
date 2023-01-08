package ru.sber.rdbms

import exception.AccountException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

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
                val amount1 = getAmount(conn, accountId1)
                if (amount1 - amount < 0) throw AccountException("Недостаточно средств на счете")

                val version1 = getVersion(conn, accountId1)
                val version2 = getVersion(conn, accountId2)

                executeOperation(conn, -amount, accountId1, version1)
                executeOperation(conn, amount, accountId2, version2)

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

    private fun executeOperation(connection: Connection, amount: Long, accountId: Long, version: Int) {
        val prepareStatement = connection.prepareStatement(
            "update my_account set amount = amount + ?, version = version + 1 where id = ? and version = ?"
        )
        prepareStatement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)

            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update")
        }
        connection.commit()
    }

    private fun getVersion(connection: Connection, id: Long): Int {
        var version = 0
        connection.autoCommit = false
        val prepareStatement = connection.prepareStatement("select version from my_account where id = ?")
        prepareStatement.use { statement ->
            statement.setLong(1, id)
            statement.executeQuery().use {
                it.next()
                version = it.getInt("version")
            }
        }
        return version
    }

    private fun getAmount(conn: Connection, accountId1: Long): Long {
        conn.prepareStatement("select amount from my_account where id = ? for update").use { statement ->
            statement.setLong(1, accountId1)
            statement.executeQuery().use {
                it.next()
                return it.getLong("amount")
            }
        }
    }
}
