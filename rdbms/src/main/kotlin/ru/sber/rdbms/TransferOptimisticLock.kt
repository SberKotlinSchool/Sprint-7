package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {

    TransferOptimisticLock().transfer(2, 1, 1000)
}
class TransferOptimisticLock {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val statSelectAccount1 = conn.prepareStatement("select * from account1 where id = ?;")
                var versionAccount1 = 0
                var amountAccount1 = 0L
                statSelectAccount1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        versionAccount1 = it.getInt("version")
                        amountAccount1 = it.getLong("amount")
                    }
                }

                val statSelectAccount2 = conn.prepareStatement("select * from account1 where id = ?;")
                var versionAccount2 = 0
                statSelectAccount2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery().use {
                        it.next()
                        versionAccount2 = it.getInt("version")
                    }
                }

                if (amountAccount1 - amount < 0)
                    throw SQLException("Недостаточно денег на счете")

                val statUpdate = conn.prepareStatement(
                    "update account1 set amount = amount - ?, version = version + 1 where id = ? and version = ?"  )
                statUpdate.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setInt(3, versionAccount1)
                    statement.addBatch()

                    statement.setLong(1, - amount)
                    statement.setLong(2, accountId2)
                    statement.setInt(3, versionAccount2)
                    statement.addBatch()

                    val updatedRows = statement.executeBatch()

                    if (updatedRows.isEmpty())
                        throw SQLException("Concurrent update")
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
