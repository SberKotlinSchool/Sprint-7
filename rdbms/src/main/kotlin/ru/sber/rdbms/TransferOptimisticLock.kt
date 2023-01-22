package ru.sber.rdbms

import ru.sber.exceptions.AccountException
import java.sql.DriverManager

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
                val selectVersionPS = conn.prepareStatement(
                    "select * from account1 where id = ?"
                )
                var version1: Long = 0
                var amount1: Long = 0
                var version2: Long = 0

                selectVersionPS.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        version1 = it.getLong("version")
                        amount1 = it.getLong("amount")
                    }
                }

                if (amount1 - amount < 0) { throw AccountException("Not enough money") }

                selectVersionPS.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        version2 = it.getLong("version")
                    }
                }

                val updateAccountAmountPS = conn.prepareStatement(
                    "UPDATE account1 SET amount = amount + ?, version = ? + 1 where id = ? AND version = ?;"
                )

                updateAccountAmountPS.use { statement ->
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
                        throw AccountException("Concurrent update")
                }
                conn.commit()
            } catch (exception: Exception) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
