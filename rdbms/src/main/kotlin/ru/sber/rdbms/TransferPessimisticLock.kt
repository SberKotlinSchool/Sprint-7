package ru.sber.rdbms

import ru.sber.rdbms.exceptions.TransferExceptions
import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(private val connection: Connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val availableAmount = getAmountOnAccount(conn, accountId1);
                validate(amount, availableAmount, accountId1);

                initAccounts(conn, accountId1, accountId2);

                transferAmount(conn, accountId1, accountId2, amount);

                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun getAmountOnAccount(conn: Connection, accountId: Long): Long {
        val prepareStatement1 = conn.prepareStatement("select amount from accounts where id = ? for update")
        prepareStatement1.use { statement ->
            statement.setLong(1, accountId);

            statement.executeQuery().use {
                it.next()
                return it.getLong("amount")
            }
        }
    }

    private fun validate(amount: Long, availableAmount: Long, accountId: Long) {
        if (availableAmount < amount)
            throw TransferExceptions("Недостаточно средств для перевода со счета $accountId")
    }

    private fun initAccounts(conn: Connection, accountId1: Long, accountId2: Long) {
        val prepareStatement1 = conn.prepareStatement("select * from accounts where id in (?,?) for update")
        prepareStatement1.use { statement ->
            statement.setLong(1, accountId1)
            statement.setLong(2, accountId2)
            statement.executeQuery()
        }
    }

    private fun transferAmount(conn: Connection, from: Long, to: Long, amount: Long) {
        val prepareStatement2 = conn.prepareStatement(
            "" +
                    "update accounts set amount = amount - ? where id = ?; " +
                    "update accounts set amount = amount + ? where id = ?;"
        )

        prepareStatement2.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, from)
            statement.setLong(3, amount)
            statement.setLong(4, to)
            statement.executeUpdate()
        }
    }
}
