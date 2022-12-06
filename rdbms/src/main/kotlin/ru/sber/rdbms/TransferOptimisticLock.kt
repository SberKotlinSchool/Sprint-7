package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        ConnectionProvider.getConnection().use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val v1 = getVersion(accountId1, conn)
                val v2 = getVersion(accountId2, conn)
                doPayment(accountId1, -amount, v1, conn)
                doPayment(accountId2, amount, v2, conn)
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    fun getVersion(id: Long, conn: Connection) : Int {
        val prepareStatement1 = conn.prepareStatement("select version from accounts where id = ?")
        var version : Int?
        prepareStatement1.use { statement ->
            statement.setLong(1, id)
            statement.executeQuery().use {
                it.next()
                return it.getInt("version")
            }
        }
        throw SQLException("account with id=$id not found");
    }

    fun doPayment(id: Long, amount: Long, version: Int, conn: Connection) {
        val statement = conn.prepareStatement("update accounts set amount = amount + ?, " +
                "version = version + 1 where id = ? and version = ?")
        statement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, id)
            statement.setInt(3, version)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update")
        }
    }
}

fun main() {
    TransferOptimisticLock().transfer(1, 3, 10)
}
