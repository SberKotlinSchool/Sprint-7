package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(val connection: Connection) {

    var accountAmount: Long = 0

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {


        connection.use { conn ->

            conn.autoCommit = false

            try {
                conn.autoCommit = false

                accountAmount = getAmount(conn, accountId1)

                if (amount > accountAmount) {
                    throw java.lang.Exception("На счете id == $accountId1, недостаточно средств для перевода")
                }

                val prepareStatement1 = conn.prepareStatement("select * from accounts where id in (?,?) for update")
                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeQuery()
                }


                val prepareStatement2 = conn.prepareStatement("""
                    update accounts set amount = amount - ? where id = ?;
                    update accounts set amount = amount + ? where id = ?;
                    """)

                prepareStatement2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, amount)
                    statement.setLong(4, accountId2)
                    statement.executeUpdate()
                }

                println("Success transfer $amount from account id == $accountId1 to account id == $accountId2")

            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.commit()
            }
        }
    }

    private fun getAmount(connection: Connection, accountId: Long): Long {

        val statement = connection.prepareStatement("select amount from accounts where id = ?")
        statement.use { statement ->
            statement.setLong(1, accountId)

            statement.executeQuery().use {
                it.next()
                return it.getLong(1)
            }
        }
    }
}
