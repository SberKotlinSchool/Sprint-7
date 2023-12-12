package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    private val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
    )

    private val queryUpdate = "UPDATE account1 SET amount = amount - ? WHERE id = ?;" +
            "UPDATE account1 SET amount = amount + ? WHERE id = ?;"

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatement = conn.prepareStatement(queryUpdate)
                prepareStatement.use { statement ->

                        statement.setLong(1, amount)
                        statement.setLong(2, accountId1)
                        statement.setLong(3, amount)
                        statement.setLong(4, accountId2)

                        statement.executeUpdate()
                }

                conn.commit()

            } catch (e: SQLException) {
                println(e.message)
                e.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}

fun main() {
    TransferConstraint().transfer(2, 1,500)
}
