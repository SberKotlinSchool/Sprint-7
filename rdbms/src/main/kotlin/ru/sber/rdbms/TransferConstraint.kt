package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    private val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "postgres")

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            conn.autoCommit = false

            try {
                val prepareStatement = conn.prepareStatement("UPDATE account SET amount = amount + ? WHERE id = ?")

                prepareStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()

                    statement.executeBatch()
                }
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
    TransferConstraint().transfer(1,2,100000)
}
