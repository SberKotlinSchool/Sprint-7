package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
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

                val getVersion = "select version from account where id = ? for update"
                var version1: Int
                conn.prepareStatement(getVersion).use { statement ->
                    statement.setLong(1, accountId1)
                    val resultSetVersion = statement.executeQuery()
                    resultSetVersion.next()
                    version1 = resultSetVersion.getInt("version")
                }

                var version2: Int
                conn.prepareStatement(getVersion).use { statement ->
                    statement.setLong(1, accountId2)
                    val resultSetVersion = statement.executeQuery()
                    resultSetVersion.next()
                    version2 = resultSetVersion.getInt("version")
                }

                conn.prepareStatement(
                    "update account set amount = amount - ?, version = ? + 1 where id = ? and version = ?"
                ).use { statement ->
                    statement.setLong(1, amount)
                    statement.setInt(2, version1)
                    statement.setLong(3, accountId1)
                    statement.setInt(4, version1)
                    statement.addBatch()

                    statement.setLong(1, -amount)
                    statement.setInt(2, version2)
                    statement.setLong(3, accountId2)
                    statement.setInt(4, version2)
                    statement.addBatch()

                    val resultSetAmount = statement.executeBatch()
                    if (resultSetAmount.isEmpty()) {
                        throw SQLException("Concurrent update")
                    }
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