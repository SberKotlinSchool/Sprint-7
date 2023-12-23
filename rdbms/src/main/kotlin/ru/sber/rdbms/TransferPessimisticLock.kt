package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val autoCommit = connection.autoCommit
        try {
            connection.autoCommit = false
            val selectStatement = connection.prepareStatement(
                "select * from account1 where id = ? for update"
            )

            selectStatement.setLong(1, accountId1)
            selectStatement.addBatch()
            selectStatement.setLong(1, accountId2)
            selectStatement.addBatch()

            selectStatement.executeBatch()

            val updateStatement = connection.prepareStatement(
                "update account1 set amount = amount + ? where id = ?"
            )
            updateStatement.use {
                updateStatement.setLong(1, -amount)
                updateStatement.setLong(2, accountId1)
                updateStatement.addBatch()

                updateStatement.setLong(1, amount)
                updateStatement.setLong(2, accountId2)
                updateStatement.addBatch()

                updateStatement.executeBatch()
            }
            connection.commit()

        } catch (e: SQLException) {
            e.printStackTrace()
            connection.rollback()
        } finally {
            connection.autoCommit = autoCommit
        }
    }
}
