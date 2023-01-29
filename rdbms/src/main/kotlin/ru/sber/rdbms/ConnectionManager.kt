package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

class ConnectionManager {
    fun getConnection(): Connection {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/kotlin?currentSchema=accounts",
            "postgres",
            "root"
        )
    }

}