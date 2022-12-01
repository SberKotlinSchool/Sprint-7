package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    connection.use {
        val autoCommit = it.autoCommit
        val transfer = TransferConstraint(it)
        try {
            it.autoCommit = false
            transfer.transfer(1, 2, 1000)
            it.commit()
            transfer.transfer(1, 2, 1000)
            it.commit()
        } catch (exception: SQLException) {
            println(exception.message)
            exception.printStackTrace()
            it.rollback()
        } finally {
            it.autoCommit = autoCommit
        }
    }
}