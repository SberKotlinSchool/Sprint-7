package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
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
                val prepareStatementSelect1 = conn.prepareStatement("select * from account1 where id = ? for update")
                prepareStatementSelect1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use { resultSet ->
                        resultSet.next()
                        val oldAmount = resultSet.getLong("amount")
                        if (oldAmount < amount) throw SQLException("Not enough money")
                    }
                }
                val prepareStatementSelect2 = conn.prepareStatement("select * from account1 where id = ? for update")
                prepareStatementSelect2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                val prepareStatementUpdate1 =
                    conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                prepareStatementUpdate1.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)

                    statement.executeUpdate()
                }
                val prepareStatementUpdate2 =
                    conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                prepareStatementUpdate2.use { statement ->
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
    TransferPessimisticLock().transfer(1, 2, 100)
    TransferPessimisticLock().transfer(2, 1, 100)
}
