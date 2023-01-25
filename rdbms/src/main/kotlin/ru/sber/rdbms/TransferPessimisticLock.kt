package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    private val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "postgres")

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val statementLock = conn.prepareStatement("SELECT * FROM account WHERE id IN (?,?) FOR UPDATE")

                statementLock.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeQuery().use {
                        while (it.next()) {
                            if (it.getLong("id") == accountId1) {
                                if (it.getLong("amount") - amount < 0) {
                                    throw SQLException("Account amount cannot be less than 0")
                                }
                            }
                        }
                    }
                }

                val updateStatement = conn.prepareStatement("UPDATE account SET amount = amount + ? WHERE id = ?")
                updateStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()

                    statement.executeBatch()
                }

                conn.commit()

            } catch (exception: SQLException) {
                exception.printStackTrace()
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}