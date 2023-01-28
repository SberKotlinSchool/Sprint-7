package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    /**
     * @param accountId1 account of substract
     * @param accountId2 account of addition
     * @param amount amount of money
     */
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )

        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                conn.prepareStatement("select amount from account where id = ? for update").use { statement ->
                    statement.setLong(1, accountId1)
                    val resultSetAmount = statement.executeQuery()
                    resultSetAmount.next()
                    val amount1 = resultSetAmount.getLong("amount")
                    if (amount1 - amount < 0) {
                        throw NotEnoughMoneyException("Not enough money on account with id:$accountId1")
                    }
                }

                conn.prepareStatement(
                    "update account set amount = amount - ? where id = ?"
                ).use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()

                    val resultSetAmount = statement.executeBatch()
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
