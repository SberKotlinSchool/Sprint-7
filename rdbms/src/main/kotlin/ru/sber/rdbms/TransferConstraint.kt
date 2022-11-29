package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { connection ->
            val statement = connection.prepareStatement(
                "update account1 set amount = amount - ? where id = ?;" + "update account1 set amount = amount + ? where id = ?;"
            )
            statement.use { statement ->
                statement.setLong(1, amount)
                statement.setLong(2, accountId1)
                statement.setLong(3, amount)
                statement.setLong(4, accountId2)
                val result = statement.executeUpdate()
                println("result = ${result}")
            }
        }
    }
}
