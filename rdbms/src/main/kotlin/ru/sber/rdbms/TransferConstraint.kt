package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {
    fun transfer(senderId: Long, receiverId: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            Constants.URL,
            Constants.USERNAME,
            Constants.PASSWORD
        )

        connection.use { conn ->
            var prepareStatement = conn.prepareStatement(
                """update bankaccount set amount = amount - $amount where id = $senderId;
                update bankaccount set amount = amount + $amount where id = $receiverId;"""
            )
            prepareStatement.use { statement -> statement.executeUpdate() }
        }
    }
}