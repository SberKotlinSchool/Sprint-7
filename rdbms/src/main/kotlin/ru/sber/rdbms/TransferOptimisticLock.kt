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
                val prepareStatement1 = conn.prepareStatement("select version from account where id = ?")
                var version1: Long = 0
                var version2: Long = 0

                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        version1 = it.getLong("version")
                    }
                }

                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery().use {
                        it.next()
                        version2 = it.getLong("version")
                    }
                }
                val prepareStatement2 = conn.prepareStatement("UPDATE account2 SET amount = amount + ?, version = ? + 1 where id = ? AND version = ?;")
                prepareStatement2.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, version1)
                    statement.setLong(3, accountId1)
                    statement.setLong(4, version1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, version2)
                    statement.setLong(3, accountId2)
                    statement.setLong(4, version2)
                    statement.addBatch()

                    val updatedRows = statement.executeBatch()

                    if (updatedRows.isEmpty())
                        throw SQLException("Concurrent update")
                }
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
}
