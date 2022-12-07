package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

object PostGresConnection {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )
}
class TransferConstraint(private val connection: Connection = PostGresConnection.connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { connection ->
            val preparedStatement = connection.prepareStatement(
                "UPDATE account2 SET amount = amount - $amount WHERE id = $accountId1;" +
                        "UPDATE account2 SET amount = amount + $amount WHERE id = $accountId2;"

            )
            preparedStatement.use {
                val result = it.executeUpdate()
                println("$result")
            }
        }
    }
}


fun main() {
    val tc = TransferConstraint()
    tc.transfer(
        1, 2, 0
    )
}
