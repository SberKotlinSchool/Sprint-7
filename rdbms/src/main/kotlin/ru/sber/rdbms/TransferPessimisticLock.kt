package ru.sber.rdbms

import ru.sber.exceptions.AccountException
import java.sql.DriverManager

class TransferPessimisticLock {
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
                val selectForUpdatePS = conn.prepareStatement(
                    "SELECT * FROM account where id = ? FOR UPDATE"
                )
                var amount1:Long = 0
                selectForUpdatePS.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        amount1 = it.getLong("amount")
                    }
                }

                if (amount1 - amount < 0) { throw AccountException("Not enough money") }

                selectForUpdatePS.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }
                val updateAccountAmountPS = conn.prepareStatement(
                    "UPDATE account SET amount = amount + ? WHERE id = ?"
                )
                updateAccountAmountPS.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()

                    statement.executeBatch()
                }
                conn.commit()
            } catch (exception: Exception) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
