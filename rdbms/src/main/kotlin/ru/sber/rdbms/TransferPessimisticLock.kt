package ru.sber.rdbms

import exception.AccountException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock() {
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
                val amount1 = getAmount(conn, accountId1)
                if (amount1 - amount < 0) throw AccountException("Недостаточно средств на счете")

                val prepareStatement2 = conn.prepareStatement("update my_account set amount = amount + ? where id = ?")
                prepareStatement2.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()
                    statement.executeBatch()
                }
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


    private fun getAmount(conn: Connection, accountId1: Long): Long {
        conn.prepareStatement("select * from my_account where id = ? for update").use { statement ->
            statement.setLong(1, accountId1)
            statement.executeQuery().use {
                it.next()
                return it.getLong("amount")
            }
        }
    }

}