package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    val connection = DriverManager.getConnection(
        JDBC_POSTGRES_DB_CONNECTION, DB_USER, DB_PASS
    )
    connection.use { conn ->
        val autoCommit = conn.autoCommit
        try {
            conn.autoCommit = false
            val prepareStatement1 = conn.prepareStatement("select * from accounts where id = 1")
            var version = 0
            prepareStatement1.use { statement ->
                statement.executeQuery().use {
                    it.next()
                    version = it.getInt("version")
                }
            }
            val prepareStatement2 = conn.prepareStatement("update accounts set amount = amount - 100, version = version + 1 where id = 1 and version = ?")
            prepareStatement2.use { statement ->
                statement.setInt(1, version)
                val updatedRows = statement.executeUpdate()
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


