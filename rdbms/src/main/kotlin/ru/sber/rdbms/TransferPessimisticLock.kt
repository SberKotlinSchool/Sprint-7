package ru.sber.rdbms

import ru.sber.rdbms.exception.ImpossibleOperationException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )
        connection.use {
            val prevAutoCommit = connection.autoCommit

            try {
                connection.autoCommit = false

                val amount1 = getAmount(connection, accountId1)
                if (amount1 < amount) throw ImpossibleOperationException(NOT_ENOUGH_MONEY)

                val statement = connection.prepareStatement(
                    "UPDATE account1 SET amount = amount + ? WHERE id = ?"
                )
                statement.use {
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()

                    statement.executeBatch()
                }
                connection.commit()
            } catch (e: SQLException) {
                println(e.message)
                connection.rollback()
            } finally {
                connection.autoCommit = prevAutoCommit
            }
        }
    }

    private fun getAmount(connection: Connection, id: Long): Long {
        val prepareStatement = connection.prepareStatement(
            "select amount from account1 where id = ?")

        prepareStatement.use { statement ->
            statement.setLong(1, id)

            val resultSet = statement.executeQuery()
            resultSet.use {
                it.next()
                return it.getLong("amount")
            }
        }
    }
}
