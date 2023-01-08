package ru.sber.rdbms

import ru.sber.rdbms.Direction.DOWN
import java.sql.DriverManager
import java.sql.SQLException

enum class Direction { UP, DOWN }

class TransferOptimisticLock {

    private val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        val account1: Account
        val account2: Account

        connection.use { conn -> conn.autoCommit = false

            try {
                account1 = getAccountInfo(accountId1)
                updateAmount(account1, amount, DOWN)

                account2 = getAccountInfo(accountId2)
                updateAmount(account2, amount, Direction.UP)

                conn.commit()

                println("Transfer $amount from account $accountId1 to account $accountId2 successfully completed.")

            } catch (exception: Exception) {
                println(exception)
                conn.rollback()
            }
        }
    }

    private fun updateAmount(acc: Account, amount: Long, direction: Direction) {

        val newAmount = when (direction) {
            DOWN -> acc.amount - amount
            Direction.UP -> acc.amount + amount
        }

        if (newAmount < 0)
            throw java.lang.Exception("There are not enough funds on account ${acc.id} to transfer")

        val stmt =
            connection.prepareStatement("update account1 set amount = ?, version = version + 1 where id = ? and version = ?")

        stmt.use { statement ->
            statement.setLong(1, newAmount)
            statement.setLong(2, acc.id)
            statement.setLong(3, acc.version)

            val cnt = statement.executeUpdate()

            if (cnt == 0)
                throw SQLException("Row version do not match, transfer is not possible")
        }
    }

    private fun getAccountInfo(accId: Long): Account {
        val stmt = connection.prepareStatement("select amount, version from account1 where id = ?")
        stmt.use { statement->
            statement.setLong(1, accId)

            statement.executeQuery().use {
                it.next()
                return Account(accId, it.getLong("amount"), it.getLong("version"))
            }
        }
    }

}
