package ru.sber.rdbms

import ru.sber.rdbms.exception.ImpossibleOperationException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

const val NOT_ENOUGH_MONEY = "Not enough money"

class TransferOptimisticLock {
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

                val account1ver = getVersion(connection, accountId1)
                val account2ver = getVersion(connection, accountId2)

                executeOperation(connection, -amount, accountId1, account1ver)
                executeOperation(connection, amount, accountId2, account2ver)

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

    private fun getVersion(connection: Connection, id: Long): Int {
        val prepareStatement = connection.prepareStatement(
            "select version from account1 where id = ?")

        prepareStatement.use { statement ->
            statement.setLong(1, id)

            val resultSet = statement.executeQuery()
            resultSet.use {
                it.next()
                return it.getInt("version")
            }
        }
    }

    private fun executeOperation(connection: Connection, amount: Long, accountId: Long, version: Int) {
        val prepareStatement = connection.prepareStatement(
            "update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?"
        )
        prepareStatement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)

            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw SQLException("Concurrent update")
        }
        connection.commit()
    }
}
