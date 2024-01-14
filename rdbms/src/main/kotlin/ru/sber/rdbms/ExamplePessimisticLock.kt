package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

/**
create table accounts
(
id bigserial constraint account_pk primary key,
amount int
);
 */
fun main() {
    val connection = DriverManager.getConnection(
        JDBC_POSTGRES_DB_CONNECTION, DB_USER, DB_PASS
    )
    connection.use { conn ->
        val autoCommit = conn.autoCommit
        try {
            conn.autoCommit = false
            val prepareStatement1 = conn.prepareStatement("select * from accounts where id = 1 for update")
            prepareStatement1.use { statement ->
                statement.executeQuery()
            }
            val prepareStatement2 = conn.prepareStatement("update accounts set amount = amount - 100 where id = 1")
            prepareStatement2.use { statement ->
                statement.executeUpdate()
            }
            conn.commit()
        } catch (exception: SQLException) {
            println(exception.message)
            conn.rollback()
        } finally {
            conn.autoCommit = autoCommit
        }
    }
}


