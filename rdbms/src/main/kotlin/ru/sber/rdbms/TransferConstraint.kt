package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long): Boolean {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )

        try {
            connection.use { conn ->
                val prepareStatement = conn.prepareStatement(
                    "update account1 set amount = amount - ? where id = ?;" +
                            "update account1 set amount = amount + ? where id = ?"
                )

                prepareStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, amount)
                    statement.setLong(4, accountId2)

                    statement.executeUpdate()
                }
            }
            return true
        } catch (e: SQLException) {
            return false
        }
    }
}