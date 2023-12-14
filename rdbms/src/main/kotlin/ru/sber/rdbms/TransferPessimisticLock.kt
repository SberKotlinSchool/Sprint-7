package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {

    TransferPessimisticLock().transfer(1, 2, 100)
}
class TransferPessimisticLock {

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
                val statSelectAccount1 = conn.prepareStatement("select * from account1 where id = ? for update;")
                var amountAccount1 = 0L
                statSelectAccount1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        amountAccount1 = it.getLong("amount")
                    }
                }

                val statSelectAccount2 = conn.prepareStatement("select * from account1 where id = ? for update;")
                statSelectAccount2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                if (amountAccount1 - amount < 0)
                    throw SQLException("Недостаточно денег на счете")

                val statUpdate = conn.prepareStatement(
                    "update account1 set amount = amount - ? where id = ?"  )
                statUpdate.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, - amount)
                    statement.setLong(2, accountId2)
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
