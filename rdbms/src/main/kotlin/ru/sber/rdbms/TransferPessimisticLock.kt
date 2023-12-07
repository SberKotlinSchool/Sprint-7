package ru.sber.rdbms

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
            val autoComit = connection.autoCommit
            try {
                connection.autoCommit = false
                val statementLock = connection.prepareStatement("select * from account where id in (?, ?) for update")
                statementLock.use { preparedStatement ->
                    preparedStatement.setLong(1, accountId1)
                    preparedStatement.setLong(2, accountId2)
                    preparedStatement.executeQuery().use {
                        while (it.next()) {
                            if (it.getLong("id") == accountId1) {
                                if (it.getLong("amount") - amount < 0) {
                                    throw SQLException("Баланс счета не может быть меньше нуля")
                                }
                            }
                        }
                    }
                }
                val statementUpdate = connection.prepareStatement("update account set amount = amount + ? where id = ?")
                statementUpdate.use { preparedStatement ->
                    preparedStatement.setLong(1, -amount)
                    preparedStatement.setLong(2, accountId1)
                    preparedStatement.addBatch()

                    preparedStatement.setLong(1, amount)
                    preparedStatement.setLong(2, accountId2)
                    preparedStatement.addBatch()

                    preparedStatement.executeBatch()
                }
                connection.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                connection.rollback()
            } finally {
                connection.autoCommit = autoComit
            }
        }
    }
}
