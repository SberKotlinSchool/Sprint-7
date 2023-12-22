package ru.sber.rdbms

import java.sql.DriverManager

@Suppress("SqlResolve", "SqlNoDataSourceInspection")
class TransferPessimisticLock {
    private companion object {
        private const val GET_CURRENT_ACCOUNT_WITH_LOCK_QUERY = "select * from account1 where id = ? for update"
        private const val UPDATE_ACCOUNT_QUERY = "update account1 set amount = amount + ? where id = ?"
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
                val lockingStatement = connection.prepareStatement(GET_CURRENT_ACCOUNT_WITH_LOCK_QUERY)
                lockingStatement.setLong(1, accountId1)
                lockingStatement.addBatch()
                lockingStatement.setLong(1, accountId2)
                lockingStatement.executeBatch()

                val updatingStatement = connection.prepareStatement(UPDATE_ACCOUNT_QUERY)
                updatingStatement.setLong(1, -amount)
                updatingStatement.setLong(2, accountId1)
                updatingStatement.addBatch()
                updatingStatement.setLong(1, amount)
                updatingStatement.setLong(2, accountId2)
                updatingStatement.addBatch()
                updatingStatement.executeBatch()

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
