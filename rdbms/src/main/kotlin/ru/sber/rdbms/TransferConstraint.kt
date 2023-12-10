package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                val decStatement = conn.prepareStatement(
                    "" +
                            "update account1 set amount = amount - ? where id = ?"
                )
                decStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }
                val incStatement = conn.prepareStatement(
                    "" +
                            "update account1 set amount = amount + ? where id = ?"
                )
                incStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw SQLException("Concurrent update")
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
    TransferConstraint().transfer(1, 2, 300)
}
