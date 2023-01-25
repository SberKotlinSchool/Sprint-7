package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    private val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "postgres")

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        connection.use { conn ->
            val autoCommit = conn.autoCommit
            conn.autoCommit = false

            try {
                var version1: Long
                var version2: Long

                val statementGetAccount1 = conn.prepareStatement("SELECT * FROM account WHERE id = ?")

                statementGetAccount1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use { result ->
                        result.next()
                        result.getLong("amount")
                            .let { if (it - amount < 0) throw SQLException("Account amount cannot be less than 0") }
                        version1 = result.getLong("version")
                    }
                }

                val statementGetAccount2 = conn.prepareStatement("SELECT * FROM account WHERE id = ?")
                statementGetAccount2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery().use { result ->
                        result.next()
                        version2 = result.getLong("version")
                    }
                }

                val statementUpdateAccounts =
                    conn.prepareStatement(
                        """
                        UPDATE account SET amount = amount + ?, version = version + 1 
                        WHERE id = ? AND version = ?
                        """
                    )

                statementUpdateAccounts.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, version1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setLong(3, version2)
                    statement.addBatch()

                    val executedBatch = statement.executeBatch()
                    if (executedBatch.isEmpty()) throw SQLException("Concurrent update")
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