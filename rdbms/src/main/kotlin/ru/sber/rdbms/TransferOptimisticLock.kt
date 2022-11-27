package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock(private val connection: Connection = PostGresConnection.connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { connection ->
            val autoCommit = connection.autoCommit
            try {
                connection.autoCommit = false

                var accountId1version: Long = 0
                var accountId2version: Long = 0

                val getVersionId1Statement = connection.prepareStatement("select version from account2 where id = $accountId1")
                val getVersionId2Statement = connection.prepareStatement("select version from account2 where id = $accountId2")

                getVersionId1Statement.executeQuery().use {
                    it.next()
                    accountId1version = it.getLong("version")
                }

                getVersionId2Statement.executeQuery().use {
                    it.next()
                    accountId2version= it.getLong("version")
                }

                val updateStatement = connection.prepareStatement(
                    "UPDATE account2 SET amount = amount + ?, version = ? + 1 where id = ? AND version = ?;"
                )
                updateStatement.use { updateStatement ->
                    updateStatement.setLong(1, -amount)
                    updateStatement.setLong(2, accountId1version)
                    updateStatement.setLong(3, accountId1)
                    updateStatement.setLong(4, accountId1version)
                    updateStatement.addBatch()

                    updateStatement.setLong(1, amount)
                    updateStatement.setLong(2, accountId2version)
                    updateStatement.setLong(3, accountId2)
                    updateStatement.setLong(4, accountId2version)
                    updateStatement.addBatch()

                    val updatedRows = updateStatement.executeBatch()

                    if (updatedRows.isEmpty()) {
                        throw SQLException("Concurrent update")
                    }
                }
                connection.commit()
            } catch (sqlException: SQLException) {
                println(sqlException.message)
                sqlException.printStackTrace()
                connection.rollback()
            } finally {
                connection.autoCommit =  autoCommit
            }
        }
    }
}

fun main() {
    val tc = TransferOptimisticLock()
    tc.transfer(2, 1, 500)
}