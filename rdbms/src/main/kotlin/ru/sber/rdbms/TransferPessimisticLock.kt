package ru.sber.rdbms

import ru.sber.rdbms.exception.AmountException
import ru.sber.rdbms.exception.IntegrityException
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )

        connection.use { conn ->
            val collectingInfoStatement =
                conn.prepareStatement("SELECT * FROM accounts.account WHERE id IN (?, ?) FOR UPDATE")
            val amounts = LongArray(2)
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                collectingInfoStatement.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeQuery().use {
                        var i = 0
                        while (it.next()) {
                            amounts[i] = it.getLong("amount")
                            i++
                        }
                    }
                }

                if (amounts[0] < amount) {
                    throw AmountException()
                }

                val transferStatement =
                    conn.prepareStatement("UPDATE accounts.account SET amount = amount + ? WHERE id = ?")
                transferStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
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
