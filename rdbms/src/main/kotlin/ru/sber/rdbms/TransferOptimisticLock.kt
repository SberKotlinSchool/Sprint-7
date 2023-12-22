package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

@Suppress("SqlResolve", "SqlNoDataSourceInspection")
class TransferOptimisticLock {
    private companion object {
        private const val GET_CURRENT_ACCOUNT_QUERY = "select * from account1 WHERE id = ?"
        private const val UPDATE_ACCOUNT_QUERY =
            "update account1 SET amount = amount + ?, version = version + 1 WHERE id = ? and version = ?"
    }

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/kotlin_school_db",
            "postgres",
            "postgres"
        ).use { connection ->
            val sourceAutoCommit = connection.autoCommit
            try {
                connection.autoCommit = false
                updateAccount(connection, accountId1, -amount)
                updateAccount(connection, accountId2, amount)
                connection.commit()
            } catch (ex: Exception) {
                ex.printStackTrace()
                connection.rollback()
            } finally {
                connection.autoCommit = sourceAutoCommit
            }
        }
    }

    private fun updateAccount(connection: Connection, accountId: Long, amount: Long) {
        var version: Int
        connection.prepareCall(GET_CURRENT_ACCOUNT_QUERY).use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use { resultSet ->
                resultSet.next()
                version = resultSet.getInt("version")
            }
        }

        connection.prepareStatement(UPDATE_ACCOUNT_QUERY).use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)
            if (statement.executeUpdate() == 0) {
                throw RuntimeException("Concurrent update error")
            }
        }
    }
}
