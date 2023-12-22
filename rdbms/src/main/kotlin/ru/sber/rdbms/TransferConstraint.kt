package ru.sber.rdbms

import java.sql.DriverManager

@Suppress("SqlResolve", "SqlNoDataSourceInspection")
class TransferConstraint {
    private companion object {
        private const val SUB_TRANSFER_QUERY = "update account1 set amount - ? WHERE id = ?"
        private const val ADD_TRANSFER_QUERY = "update account1 set amount + ? WHERE id = ?"
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
                val subStatement = connection.prepareStatement(SUB_TRANSFER_QUERY)
                subStatement.setLong(1, amount)
                subStatement.setLong(2, accountId1)
                if (subStatement.executeUpdate() == 0) {
                    throw RuntimeException("Concurrent update error")
                }
                val addStatement = connection.prepareStatement(ADD_TRANSFER_QUERY)
                addStatement.setLong(1, amount)
                addStatement.setLong(2, accountId2)
                if (addStatement.executeUpdate() == 0) {
                    throw RuntimeException("Concurrent update error")
                }
                connection.commit()
            } catch (ex: Exception) {
                ex.printStackTrace()
                connection.rollback()
            } finally {
                connection.autoCommit = sourceAutoCommit
            }
        }
    }
}
