package ru.sber.rdbms

import java.sql.DriverManager

const val JDBC_POSTGRES_DB_CONNECTION: String = "jdbc:postgresql://localhost:5432/rdbms"
const val DB_USER: String = "postgres"
const val DB_PASS: String = "postgres"

fun main() {
    val connection = DriverManager.getConnection(
        JDBC_POSTGRES_DB_CONNECTION, DB_USER, DB_PASS
    )
    connection.use { conn ->
        val prepareStatement = conn.prepareStatement("select count(*) from accounts")
        prepareStatement.use { statement ->
            val resultSet = statement.executeQuery()
            resultSet.use {
                println("Есть записи: ${it.next()}")
                val result = it.getInt(1)
                println("Количество записей: $result")
            }
        }
    }
}

