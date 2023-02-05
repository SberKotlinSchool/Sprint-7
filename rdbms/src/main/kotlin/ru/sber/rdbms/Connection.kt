package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

fun connection(using: (conn: Connection) -> Unit) = DriverManager.getConnection(
    "jdbc:postgresql://localhost:5432/db",
    "postgres",
    "postgres"
).use { conn ->

    try {
        conn.autoCommit = false
        using(conn)
    } catch (exception: Exception) {
        println(exception.message)
        exception.printStackTrace()
        conn.rollback()
    } finally {
        conn.autoCommit = true
    }

}


fun Connection.execUpdate(sql: String, using: (PreparedStatement) -> Unit): Int {
    return this.prepareStatement(sql).use { ps ->
        using(ps)
        ps.executeUpdate()
    }
}

fun <T> Connection.query(sql: String, using: (PreparedStatement) -> Unit, extract: (ResultSet) -> T): T {
    this.prepareStatement(sql).use { statement ->
        using(statement)
        return statement.executeQuery().use {
            it.next()
            extract(it)
        }
    }
}