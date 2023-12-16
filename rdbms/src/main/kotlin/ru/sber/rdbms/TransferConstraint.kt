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

                val debitOperation = conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                debitOperation.setLong(1, amount)
                debitOperation.setLong(2, accountId1)

                debitOperation.use { statement ->
                    statement.executeUpdate()
                }

                val creditOperation = conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                creditOperation.setLong(1, amount)
                creditOperation.setLong(2, accountId2)

                creditOperation.use { statement ->
                    statement.executeUpdate()
                }
                conn.commit()
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
