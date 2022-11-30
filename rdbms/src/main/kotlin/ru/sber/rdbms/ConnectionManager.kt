package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

open class ConnectionManager {
    open fun getConnection(): Connection {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
    }

}