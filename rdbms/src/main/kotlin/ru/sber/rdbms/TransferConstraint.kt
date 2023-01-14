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
            val autoCommit = conn.autoCommit
            conn.autoCommit = false
            //take from the first account
            var prepareStatement1 = conn.prepareStatement("select * from accounts where id = $accountId1")
            prepareStatement1.use { statement ->
                val resultSet = statement.executeQuery()
                resultSet.use {
                    println("Has result: ${it.next()}")
                    val result = it.getInt("amount")
                    println("Execution result: $result")
                }
            }
            var prepareStatement2 = conn.prepareStatement("update accounts set amount = amount - $amount where id = $accountId1")
            prepareStatement2.use { statement ->
                statement.executeUpdate()
            }
            // add to the second account
            prepareStatement1 = conn.prepareStatement("select * from accounts where id = $accountId2")
            prepareStatement1.use { statement ->
                val resultSet = statement.executeQuery()
                resultSet.use {
                    println("Has result: ${it.next()}")
                    val result = it.getInt("amount")
                    println("Execution result: $result")
                }
            }
            prepareStatement2 = conn.prepareStatement("update accounts set amount = amount + $amount where id = $accountId2")
            prepareStatement2.use { statement ->
                statement.executeUpdate()
            }

            conn.autoCommit = autoCommit
        }
    }
}