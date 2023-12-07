package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    TransferConstraint().transfer(1, 2, 10000)
}

class TransferConstraint {

    private val dbConnection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        dbConnection.use { connection ->
            val autoCommit = connection.autoCommit
            connection.autoCommit = false

            try {
                val prepareStatement = connection.prepareStatement("update account set amount = amount + ? where id = ?")

                prepareStatement.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()

                    statement.executeBatch()
                }
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                connection.rollback()
            }finally {
                connection.autoCommit = autoCommit
            }
        }
    }
}
