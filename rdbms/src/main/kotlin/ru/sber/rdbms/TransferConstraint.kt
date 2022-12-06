package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferConstraint(val connection : Connection) {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            conn.autoCommit = false

            try {
                val prepareStatement = conn.prepareStatement(
                    """
                    update accounts set amount = amount - ? where id = ?;
                    update accounts set amount = amount + ? where id = ?;
                    """)
                prepareStatement.use {
                    it.setLong(1, amount)
                    it.setLong(2,accountId1)
                    it.setLong(3,amount)
                    it.setLong(4,accountId2)

                    prepareStatement.executeUpdate()

                    println("Success transfer $amount from account id == $accountId1 to account id == $accountId2")
                }

            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.commit()
            }

        }


    }
}
