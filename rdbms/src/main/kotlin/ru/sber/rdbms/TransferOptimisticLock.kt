package ru.sber.rdbms

import ru.sber.rdbms.exception.AmountException
import ru.sber.rdbms.exception.IntegrityException
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    fun transfer(sourceAccountId: Long, targetAccountId: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres"
        )

        connection.use { conn ->
            val collectingInfoStatement = conn.prepareStatement("SELECT * FROM accounts.account WHERE id in (?, ?)")
            val versions = IntArray(2)
            val amounts = LongArray(2)
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                collectingInfoStatement.use { statement ->
                    statement.setLong(1, sourceAccountId)
                    statement.setLong(2, targetAccountId)
                    statement.executeQuery().use {
                        var i = 0
                        while (it.next()) {
                            versions[i] = it.getInt("version")
                            amounts[i] = it.getLong("amount")
                            i++
                        }
                    }
                }

                if (amounts[0] < amount) {
                    throw AmountException()
                }

                // Для теста IntegrityException
//                val versionStatement =
//                    conn.prepareStatement("UPDATE accounts.account SET version = version + 1 WHERE id = ?")
//                versionStatement.use { statement ->
//                    statement.setLong(1, sourceAccountId)
//                    statement.executeUpdate()
//                }

                val transferStatement =
                    conn.prepareStatement("UPDATE accounts.account SET amount = amount + ?, version = version + 1 WHERE id = ? AND version = ?")
                transferStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, sourceAccountId)
                    statement.setInt(3, versions[0])
                    statement.addBatch()
                    statement.setLong(1, amount)
                    statement.setLong(2, targetAccountId)
                    statement.setInt(3, versions[1])
                    statement.addBatch()

                    val updatedRows = statement.executeBatch()
                    if (updatedRows.contains(0)) {
                        throw IntegrityException()
                    }
                }
                conn.commit()
            } catch (e: SQLException) {
                println(e.printStackTrace())
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
