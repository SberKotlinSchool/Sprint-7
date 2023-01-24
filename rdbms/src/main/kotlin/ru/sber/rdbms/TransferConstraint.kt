package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val statement = conn.prepareStatement(
                "update account1 set amount = amount - ? where id = ?;"
                        + "update account1 set amount = amount + ? where id = ?;"
            )
            statement.use { stat ->
                stat.setLong(1, amount)
                stat.setLong(2, accountId1)
                stat.setLong(3, amount)
                stat.setLong(4, accountId2)
                val result = stat.executeUpdate()
                println("result = $result")
            }
        }
    }
}

fun main() {
    TransferConstraint().transfer(1,2,200)
}
