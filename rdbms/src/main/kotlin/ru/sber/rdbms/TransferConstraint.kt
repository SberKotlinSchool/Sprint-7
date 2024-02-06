package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres", "postgres"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.autoCommit = false;
        connection.use { conn ->
            try {
                val debit = conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                debit.setLong(1, amount)
                debit.setLong(2, accountId1)
                debit.executeUpdate()

                val credit = conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                credit.setLong(1, amount)
                credit.setLong(2, accountId2)
                credit.executeUpdate()

                conn.commit()
            } catch (exception: SQLException) {
                println(exception)
                conn.rollback()
            }
        }
    }
}
