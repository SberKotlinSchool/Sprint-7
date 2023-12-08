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
                val prepareStatementSelectForUpdate1 = conn.prepareStatement(querySelectForUpdate)
                prepareStatementSelectForUpdate1.use { statement ->
                    statement.setLong(1, accountId1)

                    statement.executeQuery().use {
                        it.next()
                        val actualAmount = it.getLong("amount")
                        if (amount > actualAmount) throw SQLException("Не хватает денег на счете $accountId1")
                    }
                }
                val prepareStatementSelectForUpdate2 = conn.prepareStatement(querySelectForUpdate)
                prepareStatementSelectForUpdate2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                val prepareStatementUpdateAccount1 =
                        conn.prepareStatement("UPDATE account1 SET amount = amount - ? WHERE id = ?")
                prepareStatementUpdateAccount1.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)

                    statement.executeUpdate()
                }
                val prepareStatementUpdateAccount2 =
                        conn.prepareStatement("UPDATE account1 SET amount = amount + ? WHERE id = ?")
                prepareStatementUpdateAccount2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)

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
