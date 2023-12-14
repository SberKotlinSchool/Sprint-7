package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val sql = """
            SELECT amount, version
            FROM account
            WHERE id = $accountId1 FOR UPDATE;
            
            UPDATE account
            SET amount = amount - $amount
            WHERE id = $accountId1;
            
            UPDATE account
            SET amount = amount + $amount
            WHERE id = $accountId2;
        """
        executeUpdateWithOptimisticLock(sql)
    }

    private fun executeUpdateWithOptimisticLock(sql: String) {
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
