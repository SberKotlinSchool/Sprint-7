package ru.sber.rdbms

import java.sql.Connection

class TransferConstraint(private val connection: Connection) {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val prepareStatement = conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
            prepareStatement.use { statement ->
                statement.setLong(1, -amount)
                statement.setLong(2, accountId1)
                statement.addBatch()
                statement.setLong(1, amount)
                statement.setLong(2, accountId2)
                statement.addBatch()
                statement.executeBatch()
            }
        }
    }
}
