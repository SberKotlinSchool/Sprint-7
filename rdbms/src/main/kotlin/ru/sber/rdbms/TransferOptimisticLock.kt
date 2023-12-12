package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {

    private val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
    )

    private val querySelect = "SELECT * FROM account1 WHERE id = ?;"

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatementSelectAccount1 = conn.prepareStatement(querySelect)
                var versionAccount1 = 0
                prepareStatementSelectAccount1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        versionAccount1 = it.getInt("version")
                        val actualAmount = it.getLong("amount")
                        if (amount > actualAmount) throw SQLException("Не хватает денег на счете $accountId1")
                    }
                }

                val prepareStatementSelectAccount2 = conn.prepareStatement(querySelect)
                var versionAccount2 = 0
                prepareStatementSelectAccount2.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        versionAccount2 = it.getInt("version")
                    }
                }

                val prepareStatementUpdateAccount1 =
                        conn.prepareStatement("UPDATE account1 SET amount = amount - ?, version = version + 1 WHERE id = ? AND version = ?")
                prepareStatementUpdateAccount1.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setInt(3, versionAccount1)

                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0) throw SQLException("Параллельное обновление акаунта $accountId1")
                }

                val prepareStatementUpdateAccount2 =
                        conn.prepareStatement("update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")
                prepareStatementUpdateAccount2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setInt(3, versionAccount2)

                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0) throw SQLException("Параллельное обновление акаунта $accountId2")
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
    TransferOptimisticLock().transfer(3, 4,500)
}
