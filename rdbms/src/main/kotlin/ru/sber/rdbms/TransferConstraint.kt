package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {

    private val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "s3cr3t"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        connection.use { conn ->
            try {
                conn.autoCommit = false
                conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                    .use { statement ->
                        statement.setLong(1, amount)
                        statement.setLong(2, accountId1)
                        statement.execute()
                }

                conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                    .use { statement ->
                        statement.setLong(1, amount)
                        statement.setLong(2, accountId2)
                        statement.execute()
                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }
}
