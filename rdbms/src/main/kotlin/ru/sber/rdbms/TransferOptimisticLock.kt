package ru.sber.rdbms

import ru.sber.rdbms.exception.NegativeBalanceException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class TransferOptimisticLock {
    private val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "qwerty"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val currentBalance = getBalanceByID(connection, accountId1)
                if (currentBalance - amount < 0)
                    throw NegativeBalanceException("Недостаточно средств на счете")

                val listQuery = buildUpdateQuery(conn, accountId1, accountId2, amount)

                for (query in listQuery) {
                    val updatedRows = query.executeUpdate()
                    if (updatedRows == 0)
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

    private fun getVersionByID(connection: Connection, id: Long): Int {
        val prepareStatement1 = connection.prepareStatement("select version from account where id = ?")
        prepareStatement1.setLong(1, id)

        var version: Int

        prepareStatement1.use { statement ->
            statement.executeQuery().use {
                it.next()
                version = it.getInt("version")
            }
        }
        return version
    }

    private fun getBalanceByID(connection: Connection, id: Long): Long {
        val prepareStatement = connection.prepareStatement("select amount from account where id = ?")
        prepareStatement.setLong(1, id)

        var resultSet = prepareStatement.executeQuery()
        val amount: Long

        resultSet.use {
            it.next()
            amount = it.getInt(1).toLong()
        }
        return amount
    }

    private fun buildUpdateQuery(connection: Connection, accountId1: Long,
                                 accountId2: Long, amount: Long): MutableList<PreparedStatement> {
        val listQuery: MutableList<PreparedStatement> = mutableListOf()

        var version = getVersionByID(connection, accountId1)

        var prepareStatement = connection.prepareStatement("update account set amount = amount - ?, version = version + 1 where id = ? and version = ?")
        prepareStatement.setLong(1, amount)
        prepareStatement.setInt(2, version)
        prepareStatement.setLong(3, accountId1)

        listQuery.add(prepareStatement)

        version = getVersionByID(connection, accountId2)

        prepareStatement = connection.prepareStatement("update account set amount = amount + ?, version = version + 1 where id = ? and version = ?")
        prepareStatement.setLong(1, amount)
        prepareStatement.setInt(2, version)
        prepareStatement.setLong(3, accountId2)

        listQuery.add(prepareStatement)

        return listQuery
    }
}
