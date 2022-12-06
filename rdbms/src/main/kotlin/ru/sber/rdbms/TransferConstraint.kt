package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

object ConnectionProvider {
    fun getConnection() = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/kotlin",
        "postgres",
        "pasha222233"
    )
}

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        ConnectionProvider.getConnection().use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val statement = conn.prepareStatement(
                    "update accounts set amount = amount - ? where id = ?;\n " +
                            "update accounts set amount = amount + ? where id = ?;"
                )
                statement.use { s ->
                    s.setLong(1, amount)
                    s.setLong(2, accountId1)
                    s.setLong(3, amount)
                    s.setLong(4, accountId2)
                    statement.executeUpdate()
                    conn.commit()
                }
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
