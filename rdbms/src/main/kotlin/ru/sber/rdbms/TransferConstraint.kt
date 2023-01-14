package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {
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
        val prepStatementAdd = "update account set amount = amount - ? where id=?;"
        val prepStatementSubtract = "update account set amount = amount + ? where id=?;"
        connection.use { conn ->
            val preparedStatement = conn.prepareStatement(
                prepStatementAdd + prepStatementSubtract
            )
            preparedStatement.use { statement ->
                statement.setLong(1, amount)
                statement.setLong(2, accountId1)
                statement.setLong(3, amount)
                statement.setLong(4, accountId2)
                val result = preparedStatement.executeUpdate()
                println("result = ${result}")
            }
        }
    }
}
