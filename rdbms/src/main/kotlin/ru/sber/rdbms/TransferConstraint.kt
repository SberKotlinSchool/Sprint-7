package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )

        val senderStatement = "UPDATE accounts.account SET amount = amount - ? where id = ?;"
        val recipientStatement = "UPDATE accounts.account SET amount = amount + ? where id = ?;"
        connection.use { conn ->
            val transferStatement = conn.prepareStatement(senderStatement + recipientStatement)
            transferStatement.use { statement ->
                statement.setLong(1, amount)
                statement.setLong(2, accountId1)
                statement.setLong(3, amount)
                statement.setLong(4, accountId2)

                val autoCommit = conn.autoCommit
                try {
                    conn.autoCommit = false
                    statement.executeUpdate()
                    conn.commit()
                } catch (e: SQLException) {
                    println(e.printStackTrace())
                    conn.rollback()
                } finally {
                    conn.autoCommit = autoCommit
                }
            }
        }
    }
}
