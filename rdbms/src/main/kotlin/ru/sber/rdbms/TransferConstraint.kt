package ru.sber.rdbms

import java.sql.Connection

class TransferConstraint(private val connection: Connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val prepareStatement = conn.prepareStatement(
                "" +
                        "update accounts set amount = amount - ? where id = ?;" +
                        "update accounts set amount = amount + ? where id = ?;"
            )

            prepareStatement.use { statement ->

                statement.setLong(1, amount)
                statement.setLong(2, accountId1)
                statement.setLong(3, amount)
                statement.setLong(4, accountId2)

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
