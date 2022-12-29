package ru.sber.rdbms

import ru.sber.rdbms.exception.NegativeBalanceException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class TransferPessimisticLock {

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

                for (pairQuery in listQuery) {
                    pairQuery.first.use { statement ->
                        statement.executeQuery()
                    }

                    pairQuery.second.use { statement ->
                        statement.executeUpdate()
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
                                 accountId2: Long, amount: Long): MutableList<Pair<PreparedStatement, PreparedStatement>> {
        val listQuery: MutableList<Pair<PreparedStatement, PreparedStatement>> = mutableListOf()

        val prepareStatementSelect1 = connection.prepareStatement("select * from account where id = ? for update")
        prepareStatementSelect1.setLong(1, accountId1)


        var prepareStatementUpdate = connection.prepareStatement("update account set amount = amount - ? where id = ?")
        prepareStatementUpdate.setLong(1, amount)
        prepareStatementUpdate.setLong(2, accountId1)

        listQuery.add(Pair(prepareStatementSelect1, prepareStatementUpdate))

        val prepareStatementSelect2 = connection.prepareStatement("select * from account where id = ? for update")
        prepareStatementSelect2.setLong(1, accountId2)

        prepareStatementUpdate = connection.prepareStatement("update account set amount = amount + ? where id = ?")
        prepareStatementUpdate.setLong(1, amount)
        prepareStatementUpdate.setLong(2, accountId2)


        listQuery.add(Pair(prepareStatementSelect2, prepareStatementUpdate))

        return listQuery
    }
}
