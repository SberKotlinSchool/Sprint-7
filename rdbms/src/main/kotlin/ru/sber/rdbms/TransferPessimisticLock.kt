package ru.sber.rdbms

import ru.sber.rdbms.exception.AccountException
import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(private val connectionManager: ConnectionManager) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connectionManager.getConnection().use { conn ->
            try {
                conn.autoCommit = false
                val amount1 = getAmount(conn, accountId1)
                if (amount1 - amount < 0) throw AccountException("Недостаточно средств")

                getAmount(conn, accountId2)

                update(conn, amount * (-1), accountId1)
                update(conn, amount, accountId2)

                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }

    private fun update(conn: Connection, amount: Long, accountId1: Long) {
        val prepareStatement2 = conn.prepareStatement("update account1 set amount = amount + ?  where id = ?")
        prepareStatement2.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId1)
            statement.executeUpdate()
        }
    }

    private fun getAmount(conn: Connection, accountId1: Long): Long {
        conn.prepareStatement("select * from account1 where id = ? for update").use { statement ->
            statement.setLong(1, accountId1)
            statement.executeQuery().use {
                it.next()
                return it.getLong("amount")
            }
        }
    }
}
