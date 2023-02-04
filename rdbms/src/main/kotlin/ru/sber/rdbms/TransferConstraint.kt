package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    private val accountWithdrawQuery = "update account1 set amount = amount - ? where id = ?"
    private val accountDepositQuery = "update account1 set amount = amount + ? where id = ?"
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/rdbms",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val withdrawPS = conn.prepareStatement(accountWithdrawQuery)
                withdrawPS.setLong(1, amount)
                withdrawPS.setLong(2, accountId1)
                val withdrawUpdated = withdrawPS.executeUpdate()
                if (withdrawUpdated == 0)
                    throw SQLException("Concurrent update")

                val depositPS = conn.prepareStatement(accountDepositQuery)
                depositPS.setLong(1, amount)
                depositPS.setLong(2, accountId2)
                val depositUpdated = depositPS.executeUpdate()
                if (depositUpdated == 0)
                    throw SQLException("Concurrent update")

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
}
