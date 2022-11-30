package ru.sber.rdbms

import ru.sber.rdbms.exception.AccountException
import java.sql.Connection
import java.sql.DriverManager

class TransferOptimisticLock(private val connectionManager: ConnectionManager) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connectionManager.getConnection().use { conn ->
            try {
                conn.autoCommit = false
                val (amount1, version1) = getAccountInfo(conn, accountId1)
                if (amount1 - amount < 0) throw AccountException("Недостаточно средств")

                val (amount2, version2) = getAccountInfo(conn, accountId2)

                executeUpdate(
                    conn, accountId1, amount * (-1), version1
                )
                executeUpdate(
                    conn, accountId2, amount, version2
                )

                conn.commit()
            } catch (exception: Exception) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }


    private fun executeUpdate(conn: Connection, accountId: Long, amount: Long, version: Int) {
        val prepareStatement =
            conn.prepareStatement("update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")
        prepareStatement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw AccountException("Concurrent update")
        }
    }

    private fun getAccountInfo(conn: Connection, accountId1: Long): Pair<Long, Int> {
        val prepareStatement1 = conn.prepareStatement("select * from account1 where id = ?")
        prepareStatement1.use { statement ->
            statement.setLong(1, accountId1)
            statement.executeQuery().use {
                it.next()
                return it.getLong("amount") to it.getInt("version")
            }
        }
    }
}