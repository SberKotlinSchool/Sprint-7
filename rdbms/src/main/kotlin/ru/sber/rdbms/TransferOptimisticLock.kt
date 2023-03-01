package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
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
                val prepareStatementSelect1 = conn.prepareStatement("select * from account1 where id = ?")
                var version1: Int
                prepareStatementSelect1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        version1 = it.getInt("version")
                        val oldAmount = it.getLong("amount")
                        if (oldAmount < amount) throw SQLException("Not enough money")
                    }
                }

                val prepareStatementSelect2 = conn.prepareStatement("select * from account1 where id = ?")
                var version2: Int
                prepareStatementSelect2.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        version2 = it.getInt("version")
                    }
                }

                val prepareStatementUpdate1 =
                    conn.prepareStatement("update account1 set amount = amount - ?, version = version + 1 where id = ? and version = ?")
                prepareStatementUpdate1.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setInt(3, version1)

                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0) throw SQLException("Concurrent update $accountId1")
                }

                val prepareStatementUpdate2 =
                    conn.prepareStatement("update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")
                prepareStatementUpdate2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setInt(3, version2)

                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0) throw SQLException("Concurrent update $accountId2")
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
    TransferOptimisticLock().transfer(1, 2 , 100)
}
