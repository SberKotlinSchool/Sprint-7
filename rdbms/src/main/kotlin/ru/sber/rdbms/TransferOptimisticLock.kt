package ru.sber.rdbms

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

                var accountId1version: Long = 0
                var accountId2version: Long = 0

                val getVersionId1Statement =
                    conn.prepareStatement("select version from account1 where id = $accountId1")
                val getVersionId2Statement =
                    conn.prepareStatement("select version from account1 where id = $accountId2")

                getVersionId1Statement.executeQuery().use {
                    it.next()
                    accountId1version = it.getLong("version")
                }

                getVersionId2Statement.executeQuery().use {
                    it.next()
                    accountId2version = it.getLong("version")
                }

                val updateStatement = conn.prepareStatement(
                    "UPDATE account1 SET amount = amount + ?, version = ? + 1 where id = ? AND version = ?;"
                )
                updateStatement.use { updates ->
                    updates.setLong(1, -amount)
                    updates.setLong(2, accountId1version)
                    updates.setLong(3, accountId1)
                    updates.setLong(4, accountId1version)
                    updates.addBatch()

                    updates.setLong(1, amount)
                    updates.setLong(2, accountId2version)
                    updates.setLong(3, accountId2)
                    updates.setLong(4, accountId2version)
                    updates.addBatch()

                    val updatedRows = updates.executeBatch()

                    if (updatedRows.isEmpty()) {
                        throw SQLException("Concurrent update")
                    }
                }
                conn.commit()
            } catch (sqlException: SQLException) {
                println(sqlException.message)
                sqlException.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

}

fun main() {
    TransferOptimisticLock().transfer(1,2,400)
}
