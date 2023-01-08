package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )

        connection.use { conn -> conn.autoCommit = false

            try {
                val stmt = conn.prepareStatement(
                    """
                    update account1 set amount = amount - ? where id = ?;
                    update account1 set amount = amount + ? where id = ?;
                    """)
                stmt.use {
                    it.setLong(1, amount)
                    it.setLong(2,accountId1)
                    it.setLong(3,amount)
                    it.setLong(4,accountId2)

                    stmt.executeUpdate()
                    conn.commit()

                    println("Transfer $amount from account $accountId1 to account $accountId2 successfully completed.")
                }

            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            }

        }
    }
}
