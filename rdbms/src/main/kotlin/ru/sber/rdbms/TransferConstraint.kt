package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/annapopova",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val prepareStatement = conn.prepareStatement(
                "update accounts set amount = amount - ? where id = ?;" +
                        "update accounts set amount = amount + ? where id = ?;"
            )
            prepareStatement.use { statement ->
                prepareStatement.setLong(1, amount)
                prepareStatement.setLong(2, accountId1)
                prepareStatement.setLong(3, amount)
                prepareStatement.setLong(4, accountId2)
                val resultSet = statement.executeQuery()
                resultSet.use {
                    println("Has result: ${it.next()}")
                    val result = it.getInt(1)
                    println("Execution result: $result")
                }
            }
        }
    }
}
