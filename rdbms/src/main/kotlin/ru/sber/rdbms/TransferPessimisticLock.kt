package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
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
                val prepareStatement1 = conn.prepareStatement("SELECT * FROM account where id = ? FOR UPDATE")
                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery()
                }

                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                val prepareStatement2 = conn.prepareStatement("UPDATE account SET amount = amount + ? WHERE id = ?")
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
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
