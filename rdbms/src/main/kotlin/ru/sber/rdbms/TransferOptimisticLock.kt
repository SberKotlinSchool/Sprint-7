package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock(val connection: Connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit

            try {
                conn.autoCommit = false

                val version1 = getRowVersion(conn, accountId1)
                val version2 = getRowVersion(conn, accountId2)

                createOperation(conn, - amount, accountId1, version1)
                createOperation(conn, + amount, accountId2, version2)

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

    private fun createOperation(conn: Connection, amount: Long, accountId: Long, version: Int) {
        val statement = conn.prepareStatement(
            "update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")

        statement.use { state ->
            state.setLong(1, amount)
            state.setLong(2, accountId)
            state.setInt(3, version)

            val updatedRows = state.executeUpdate()

            if (updatedRows == 0) {
                throw SQLException("Concurrent update")
            }
        }
    }

    private fun getRowVersion(conn: Connection, id: Long): Int {
        var version = 0
        val statement = conn.prepareStatement("select * from account1 where id = ?")
        statement.use { state ->
            state.setLong(1, id)
            state.executeQuery().use {
                it.next()
                version = it.getInt("version")
            }
        }
        return version
    }
}
