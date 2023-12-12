package ru.sber.rdbms

import ru.sber.rdbms.exceptions.AmountException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                debit(conn, accountId1, amount)
                credit(conn, accountId2, amount)

                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }

    private fun debit(conn: Connection, account: Long, amount: Long) {

        var version = 0L
        selectAccountById(conn, account).use {
            it.next()

            val amountForDebit = it.getLong("amount")
            if ((amountForDebit - amount) < 0) {
                throw AmountException();
            }
            version = it.getLong("version")
        }

        updateAmountWithOptimisticLocking(conn, account, -amount, version)
    }

    private fun credit(conn: Connection, account: Long, amount: Long) {

        var version = 0L
        selectAccountById(conn, account).use {
            it.next()
            version = it.getLong("version")
        }

        updateAmountWithOptimisticLocking(conn, account, amount, version)
    }

    private fun selectAccountById(conn: Connection, id: Long): ResultSet {
        return conn.prepareStatement("select * from account1 where id = ?").use {
            it.setLong(1, id)
            it.executeQuery()
        }
    }

    private fun updateAmountWithOptimisticLocking(conn: Connection, id: Long, amount: Long, version: Long) {
        conn.prepareStatement("update account1 set amount = amount + ?, version = version + ? where id = ? and version = ?")
            .use { statement ->
                statement.setLong(1, amount)
                statement.setLong(2, 1)
                statement.setLong(3, id)
                statement.setLong(4, version)

                val updatedRows = statement.executeUpdate()
                if (updatedRows == 0)
                    throw SQLException("Concurrent update")
            }
    }
}
