package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {

    private val dbConnection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        dbConnection.use { connection ->
            val autoCommit = connection.autoCommit
            try {
                connection.autoCommit = false
                if (accountId1 < accountId2) {
                    getTransfer(connection, accountId1, -amount)
                    getTransfer(connection, accountId2, amount)
                } else {
                    getTransfer(connection, accountId2, amount)
                    getTransfer(connection, accountId1, -amount)
                }
                connection.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                connection.rollback()
            } finally {
                connection.autoCommit = autoCommit
            }
        }
    }

    private fun getTransfer(connection: Connection, accountId: Long, amount: Long) {
        var currentAmount: Long
        val statementLock = connection.prepareStatement("select * from account where id = ? for update")
        statementLock.use { preparedStatement ->
            preparedStatement.setLong(1, accountId)
            preparedStatement.executeQuery().use {
                it.next()
                currentAmount = it.getLong("amount")
            }
        }
        if (currentAmount + amount < 0) throw SQLException("Баланс счета не может быть меньше нуля")

        val statementUpdate = connection.prepareStatement("update account set amount = ? where id = ?")
        statementUpdate.use { preparedStatement ->
            preparedStatement.setLong(1, currentAmount + amount)
            preparedStatement.setLong(2, accountId)

            preparedStatement.executeUpdate()
        }
    }
}
