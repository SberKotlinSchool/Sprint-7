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
                val decStatementSelectForUpdate = conn.prepareStatement("select * from account1 where id = ? for update")
                decStatementSelectForUpdate.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use { resultSet ->
                        resultSet.next()
                        val oldAmount = resultSet.getLong("amount")
                        if (oldAmount < amount) throw SQLException("Not enough money")
                    }
                }
                val incStatementSelectForUpdate = conn.prepareStatement("select * from account1 where id = ? for update")
                incStatementSelectForUpdate.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                val decStatementUpdate =
                    conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                decStatementUpdate.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)

                    statement.executeUpdate()
                }
                val incStatementUpdate =
                    conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                incStatementUpdate.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)

                    statement.executeUpdate()
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
}

fun main() {
    TransferPessimisticLock().transfer(1, 2, 200)
}