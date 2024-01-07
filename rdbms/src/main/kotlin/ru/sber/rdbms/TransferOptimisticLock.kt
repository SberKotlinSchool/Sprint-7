package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {

    private val update = "update account1 set amount = ?, version = version + 1 where id = ? and version = ?"
    private val versionAndAmount = "select version, amount from account1 where id = ?"
    private val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres", ""
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                var version: Int
                val ps1 = conn.prepareStatement(versionAndAmount)
                ps1.setLong(1, 1)
                ps1.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        version = it.getInt("version")
                    }
                }
                val ps2 = conn.prepareStatement(update)
                ps2.use { statement ->
                    ps2.setLong(1, 100)
                    ps2.setLong(2, 2)
                    statement.setInt(3, version)
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
}
