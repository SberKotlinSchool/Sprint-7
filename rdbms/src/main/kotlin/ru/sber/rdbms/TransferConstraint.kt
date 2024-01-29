package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {

    companion object {
        private const val decrement = "update account1 set amount = amount - ? where id = ?"
        private const val increment = "update account1 set amount = amount + ? where id = ?"
        val connection: Connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres", "postgres"
        )
    }

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val decrementPS = conn.prepareStatement(decrement)
                decrementPS.setLong(1, amount)
                decrementPS.setLong(2, accountId1)
                val decrementUpdated = decrementPS.executeUpdate()
                if (decrementUpdated == 0)
                    throw SQLException("Concurrent update")

                val incrementPS = conn.prepareStatement(increment)
                incrementPS.setLong(1, amount)
                incrementPS.setLong(2, accountId2)
                val incrementUpdated = incrementPS.executeUpdate()
                if (incrementUpdated == 0)
                    throw SQLException("Concurrent update")

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
