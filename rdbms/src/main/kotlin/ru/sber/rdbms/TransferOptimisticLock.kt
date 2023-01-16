package ru.sber.rdbms

import ru.sber.rdbms.customException.InsufficientMoneyException
import ru.sber.rdbms.dto.Account
import java.sql.Connection
import java.sql.DriverManager

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/template1",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            try {
                conn.autoCommit = false
                val account1: Account = getAссount(conn, accountId1)
                if (account1.amount - amount < 0) throw InsufficientMoneyException("Недостаточно средств на счете для перевода")

                val account2: Account = getAссount(conn, accountId2)

                executeTransfer(
                    conn, accountId1, amount * (-1), account1.version
                )
                executeTransfer(
                    conn, accountId2, amount, account2.version
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
}

fun getAссount(conn: Connection, accountId1: Long): Account {
    val prepareStatementToGetAccount = conn.prepareStatement("select * from accounts where id = ?")
    prepareStatementToGetAccount.use { statement ->
        statement.setLong(1, accountId1)
        statement.executeQuery().use {
            it.next()
            return Account(it.getLong("id"), it.getInt("amount"), it.getInt("version"))
        }
    }
}

private fun executeTransfer(conn: Connection, accountId: Long, amount: Long, version: Int) {
    val prepareStatement =
        conn.prepareStatement("update accounts set amount = amount + ?, version = version + 1 where id = ? and version = ?")
    prepareStatement.use { statement ->
        statement.setLong(1, amount)
        statement.setLong(2, accountId)
        statement.setInt(3, version)
        statement.executeUpdate()

    }
}