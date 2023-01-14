package ru.sber.rdbms

import ru.sber.rdbms.customException.InsufficientMoneyException
import java.sql.Connection
import java.sql.DriverManager

class TransferPessimisticLock {
    var accountAmount: Long = 0
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/template1",
            "postgres",
            "postgres"
        )

        connection.use { conn ->
            try {
                conn.autoCommit = false
                accountAmount = getAmount(conn, accountId1)

                if (accountAmount < amount) {
                    throw InsufficientMoneyException("Недостаточно средств на счете для перевода")
                }

                val prepareStatement =
                    conn.prepareStatement("select * from accounts where id  in (?,? ) for update")
                prepareStatement.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }
                val prepareStatement2 = conn.prepareStatement(
                    """
                     update accounts set amount = amount - ? where id = ?;
                     update accounts set amount = amount + ? where id = ?;
                     """
                )

                prepareStatement2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, amount)
                    statement.setLong(4, accountId2)
                    statement.executeUpdate()
                }
            } catch (exception: Exception) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }

    private fun getAmount(conn: Connection, accountId1: Long): Long {
        val prepareStatement = conn.prepareStatement("select amount from accounts where id = ?")
        prepareStatement.use { statement ->
            statement.setLong(1, accountId1)
            statement.executeQuery().use {
                it.next()
                return it.getLong(1)
            }
        }
    }
}
