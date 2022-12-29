package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class TransferConstraint {
    private val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "qwerty"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val queryList = buildUpdateQuery(conn, accountId1, accountId2, amount)

            for (query in queryList) {
                query.use {
                    it.executeUpdate()
                }
            }
        }
    }

    private fun buildUpdateQuery(connection: Connection, accountId1: Long,
                                 accountId2: Long, amount: Long): MutableList<PreparedStatement> {
        val listQuery: MutableList<PreparedStatement> = mutableListOf()

        var prepareStatement = connection.prepareStatement("update account set amount = amount - ? where id = ?")
        prepareStatement.setLong(1, amount)
        prepareStatement.setLong(2, accountId1)

        listQuery.add(prepareStatement)

        prepareStatement = connection.prepareStatement("update account set amount = amount + ? where id = ?")
        prepareStatement.setLong(1, amount)
        prepareStatement.setLong(2, accountId2)

        listQuery.add(prepareStatement)

        return listQuery
    }
}
