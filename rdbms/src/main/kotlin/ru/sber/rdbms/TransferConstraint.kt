package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

private const val UPDATE_SQL = """
                update account set amount = amount - ? where id = ?;
                update account set amount = amount + ? where id = ?;
            """

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            try {
                val prepareStatement = conn.prepareStatement(UPDATE_SQL.trimIndent())
                prepareStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, amount)
                    statement.setLong(4, accountId2)

                    val result = statement.executeUpdate()
                    if (result == 1) {
                        println("transfer $amount from $accountId1 to $accountId2")
                    }
                }
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
            }
        }
    }
}

fun main() {
    TransferConstraint().transfer(1, 2, 10000)
    TransferConstraint().transfer(2, 3, 100)
}
