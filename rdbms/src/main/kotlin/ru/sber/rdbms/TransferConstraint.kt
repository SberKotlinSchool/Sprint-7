package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    private val decrement = "update account1 set amount = amount - ? where id = ?"
    private val increment = "update account1 set amount = amount + ? where id = ?"
    private val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres", ""
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        try {
            connection.use { conn ->
                update(conn, accountId1, amount, decrement)
                update(conn, accountId2, amount, increment)
                conn.commit()
            }
        } catch (ex: SQLException) {
            println(ex.message)
            connection.rollback()
        }
    }

    private fun update(conn: Connection, accountId2: Long, amount: Long, sql: String) {
        val incPS = conn.prepareStatement(sql)
        incPS.setLong(1, accountId2)
        incPS.setLong(2, amount)
        incPS.use { statement ->
            val incRes = statement.executeUpdate()
            if (incRes == 0) {
                throw SQLException("Concurrent update")
            }
        }
    }
}
