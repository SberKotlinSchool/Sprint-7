package ru.sber.rdbms

import java.sql.Connection

class TransferConstraint(private val connection: Connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val preparedStatement = connection.prepareStatement(
            "" +
                    "update account set amount = amount - ? where id = ?;" +
                    "update account set amount = amount + ? where id = ?;"
        )

        preparedStatement.use {
            it.setLong(1, amount)
            it.setLong(2, accountId1)
            it.setLong(3, amount)
            it.setLong(4, accountId2)
            val result = it.executeUpdate()
            println("result = $result")
        }
    }
}