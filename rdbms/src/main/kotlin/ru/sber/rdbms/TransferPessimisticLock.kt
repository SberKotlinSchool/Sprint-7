package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(private val connection: Connection = PostGresConnection.connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val autoCommit = connection.autoCommit
        try {
            connection.autoCommit = false

            val selectStatement = connection.prepareStatement(
                "SELECT * FROM account2 where id = ? FOR UPDATE"
            )

            selectStatement.setLong(1, accountId1)
            selectStatement.addBatch()
            selectStatement.setLong(1, accountId2)
            selectStatement.addBatch()

            selectStatement.executeBatch()

            val updateStatement = connection.prepareStatement(
                "UPDATE account2 SET amount = amount + ? WHERE id = ?"
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

        } catch (exception: SQLException) {
            println("SQLError - ${exception.message}")
            connection.rollback()
        } finally {
            connection.autoCommit = autoCommit
        }
    }
}

fun main() {
    val tc = TransferPessimisticLock()

    tc.transfer(1,2, 200)
}
