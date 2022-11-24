package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferOptimisticLock(val connection: Connection) {

    private var accountRowVersion: Long = 0
    private var accountAmount: Long = 0

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        connection.use { conn ->
            conn.autoCommit = false

            try {
                accountRowVersion = getVersion(conn, accountId1)
                accountAmount = getAmount(conn, accountId1)
                changeAmount(conn, accountId1, amount)
                accountRowVersion = getVersion(conn, accountId2)
                changeAmount(conn, accountId2, amount)

                println("Success transfer $amount from account id == $accountId1 to account id == $accountId2")

            } catch (exception: SQLException) {
                println(exception)
                conn.rollback()
            } finally {
                conn.commit()
            }
        }
    }

    private fun changeAmount(connection: Connection, accountId: Long, amount: Long) {

        if (amount > accountAmount) {
            throw java.lang.Exception("На счете id == $accountId, недостаточно средств для перевода")
        }

        val prepareStatement =
            connection.prepareStatement("update accounts set amount = amount- ?, version = version + 1 where id = ? and version = ?")

        prepareStatement.use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setLong(3, accountRowVersion)

            val cnt = statement.executeUpdate()

            if (cnt == 0) {
                throw SQLException("Row version not the same, transfer unavailable")
            }
        }
    }

    private fun getVersion(connection: Connection, accountId: Long): Long {

        val prepareStatement = connection.prepareStatement("select version from accounts where id = ?")
        prepareStatement.use { statement ->
            statement.setLong(1, accountId)

            statement.executeQuery().use {
                it.next()
                return it.getLong(1)
            }
        }
    }

    private fun getAmount(connection: Connection, accountId: Long): Long {

        val prepareStatement = connection.prepareStatement("select amount from accounts where id = ?")
        prepareStatement.use { statement ->
            statement.setLong(1, accountId)

            statement.executeQuery().use {
                it.next()
                return it.getLong(1)
            }
        }
    }

}
