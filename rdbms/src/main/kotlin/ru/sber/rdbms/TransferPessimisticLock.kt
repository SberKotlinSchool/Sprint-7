package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    private val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
    )

    private val querySelectForUpdate = "SELECT * FROM account1 WHERE id = ? FOR UPDATE;"
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatementSelectForUpdate = conn.prepareStatement(querySelectForUpdate)
                prepareStatementSelectForUpdate.use { statement ->
                    statement.setLong(1, accountId1)

                    statement.executeQuery().use {
                        it.next()
                        val actualAmount = it.getLong("amount")
                        if (amount > actualAmount) throw SQLException("Не хватает денег на счете $accountId1")
                    }
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                val prepareStatementUpdate =
                        conn.prepareStatement("UPDATE account1 SET amount = amount - ? WHERE id = ?;" +
                                "UPDATE account1 SET amount = amount + ? WHERE id = ?")
                prepareStatementUpdate.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, amount)
                    statement.setLong(4, accountId2)

                    statement.executeUpdate()
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

fun main() {
    TransferPessimisticLock().transfer(6, 5,100)
}
