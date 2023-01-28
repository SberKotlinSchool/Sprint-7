package ru.sber.rdbms

import ru.sber.rdbms.exceptions.TransferExceptions
import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock(private val connection: Connection) {

    class ClientData(var accountId: Long, var version: Long)

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val client1 = ClientData(accountId1, getVersionByAccId(conn, accountId1))
                val client2 = ClientData(accountId2, getVersionByAccId(conn, accountId2))

                val availableAmount = getAmountOnAccount(conn, accountId1)

                validate(amount, availableAmount, client1.accountId)

                transferAmount(conn, client1, client2, amount);

                conn.commit()
            } catch (exception: TransferExceptions) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun getVersionByAccId(conn: Connection, accountId: Long): Long {
        val prepareStatement1 = conn.prepareStatement("select version from accounts where id = ?")
        prepareStatement1.use { statement ->
            statement.setLong(1, accountId);

            statement.executeQuery().use {
                it.next()
                return it.getLong("version")
            }
        }
    }

    private fun getAmountOnAccount(conn: Connection, accountId: Long): Long {
        val prepareStatement1 = conn.prepareStatement("select amount from accounts where id = ?")
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

    private fun transferAmount(conn: Connection, from: ClientData, to: ClientData, amount: Long)
    {
        executeTransfer(conn, from.accountId, from.version, -amount)
        executeTransfer(conn, to.accountId, to.version, amount)
    }

    private fun executeTransfer(conn: Connection, accountId: Long, version: Long, amount: Long)
    {
        val prepareStatement1 = conn.prepareStatement("update accounts set amount = amount + ? where id = ? and version = ?")
        prepareStatement1.use { statement ->
            statement.setLong(1, amount);
            statement.setLong(2, accountId);
            statement.setLong(3, version);

            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update")
        }
    }
}
