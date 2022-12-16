package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            Constants.URL,
            Constants.USERNAME,
            Constants.PASSWORD
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val prepareStatement1 = conn.prepareStatement(SQL_SELECT_AMOUNT)
                prepareStatement1.setLong(1, accountId1)
                var amount1: Int
                prepareStatement1.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        amount1 = it.getInt("amount")
                        if (amount1 - amount < 0) throw RuntimeException("Unable to withdraw $amount from account id $accountId1 : not enough money")
                    }
                }

                val prepareStatement2 = conn.prepareStatement(SQL_UPDATE_ONE_MINUS)
                prepareStatement2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    if (statement.executeUpdate() != 1)
                        throw SQLException("Concurrent update")
                }
                conn.commit()

                val prepareStatement3 = conn.prepareStatement(SQL_UPDATE_ONE_PLUS)
                prepareStatement3.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    if (statement.executeUpdate() != 1)
                        throw SQLException("Concurrent update")
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

    companion object {
        val SQL_SELECT_AMOUNT = "select amount from bankaccount where id = ?;"
        val SQL_UPDATE_ONE_PLUS = "update bankaccount set amount = amount - ?,  version = version + 1 where id = ?;"
        val SQL_UPDATE_ONE_MINUS = "update bankaccount set amount = amount + ?,  version = version - 1 where id = ?;"
    }
}

fun main(){
    TransferOptimisticLock().transfer(1,2,1)
}