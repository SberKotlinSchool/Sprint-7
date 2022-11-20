package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

open class BaseTest {

    protected fun getConnection(): Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    protected fun getAmount(conn: Connection, id: Long): Long {

        val statement = conn.prepareStatement("select * from account1  where id = ?")
        statement.use { stat ->
            stat.setLong(1, id)
            val resultSet = stat.executeQuery()
            resultSet.use {
                it.next()
                return it.getLong("amount")
            }
        }
    }
}