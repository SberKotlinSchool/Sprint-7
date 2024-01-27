package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    private val account = "select * from account1 where id = ? for update"
    private val update = "update account1 set amount = amount - ? where id = ?"
    private val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                var amountAcc1: Int;
                val ps1 = conn.prepareStatement(account)
                ps1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        amountAcc1 = it.getInt("amount")
                    }
                }
                if (amountAcc1 < amount) {
                    throw Exception("not enough money")
                }
                val ps2 = conn.prepareStatement(update)
                ps2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }
                conn.commit()
            } catch (exception: Exception) {
                println(exception.message)
                conn.rollback()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
