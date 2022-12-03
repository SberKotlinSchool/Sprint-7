package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        ConnectionProvider.getConnection().use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                lock(accountId1, conn)
                lock(accountId2, conn)
                doPayment(accountId1, -amount, conn)
                doPayment(accountId2, amount, conn)
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    fun lock(id: Long, conn: Connection) {
        val prepareStatement1 = conn.prepareStatement("select version from accounts where id = ? for update")
        prepareStatement1.use { statement ->
            statement.setLong(1, id)
            statement.executeQuery()
        }
    }

    fun doPayment(id: Long, amount: Long, conn: Connection) {
        val statement = conn.prepareStatement("update accounts set amount = amount + ?, " +
                "version = version + 1 where id = ?")
        statement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, id)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update")
        }
    }
}

fun main() {
    TransferPessimisticLock().transfer(2, 1, 50)
}
