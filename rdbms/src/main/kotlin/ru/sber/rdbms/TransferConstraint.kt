package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val sql = """
            UPDATE account
            SET amount = amount - $amount
            WHERE id = $accountId1 AND amount >= $amount;
            
            UPDATE account
            SET amount = amount + $amount
            WHERE id = $accountId2;
        """
        executeUpdate(sql)
    }

    private fun executeUpdate(sql: String) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            try {
                conn.autoCommit = false
                val statement = conn.createStatement()
                statement.executeUpdate(sql)
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }
}
