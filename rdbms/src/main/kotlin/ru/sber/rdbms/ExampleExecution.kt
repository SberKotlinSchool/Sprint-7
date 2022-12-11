package ru.sber.rdbms

import ru.sber.rdbms.Constants.Companion.URL
import ru.sber.rdbms.Constants.Companion.PASSWORD
import ru.sber.rdbms.Constants.Companion.USERNAME
import java.sql.DriverManager

fun main() {
    val connection = DriverManager.getConnection(
        URL,
        USERNAME,
        PASSWORD
    )
    connection.use { conn ->
        val prepareStatement = conn.prepareStatement("select 1")
        prepareStatement.use { statement ->
            val resultSet = statement.executeQuery()
            resultSet.use {
                println("Has result: ${it.next()}")
                val result = it.getInt(1)
                println("Execution result: $result")
            }
        }
    }
}

