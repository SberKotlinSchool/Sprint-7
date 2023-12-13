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
                conn.autoCommit = false

                val updateFromStatement = conn.prepareStatement("UPDATE account1 SET amount = amount + ? WHERE id = ?")
                updateFromStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }
                val updateSecondStatement = conn.prepareStatement("UPDATE account1 SET amount = amount + ? WHERE id = ?")
                updateSecondStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }
                conn.commit()

                println("transfer from $accountId1 to $accountId2: $amount")
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
