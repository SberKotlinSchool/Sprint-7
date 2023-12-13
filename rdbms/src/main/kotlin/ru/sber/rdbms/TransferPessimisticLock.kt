package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        var accountIdFirst = accountId1
        var accountIdSecond = accountId2
        var resultAmount = amount
        if (accountId1 > accountId2) {
            accountIdFirst = accountId2
            accountIdSecond = accountId1
            resultAmount = -amount
        }

        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )

        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val selectStatement = conn.prepareStatement("SELECT * FROM account1 WHERE id IN (?, ?) FOR UPDATE")
                selectStatement.use { statement ->
                    statement.setLong(1, accountIdFirst)
                    statement.setLong(2, accountIdSecond)
                    statement.executeQuery().use {
                        while (it.next()) {
                            if (it.getLong("id") == accountId1) {
                                val amountBefore = it.getLong("amount")
                                if (amountBefore < amount) {
                                    throw SQLException("not enough money")
                                }
                            }
                        }
                    }
                }

                val updateFromStatement = conn.prepareStatement("UPDATE account1 SET amount = amount + ?, version = version + 1 WHERE id = ?")
                updateFromStatement.use { statement ->
                    statement.setLong(1, -resultAmount)
                    statement.setLong(2, accountIdFirst)
                    statement.executeUpdate()
                }
                val updateSecondStatement = conn.prepareStatement("UPDATE account1 SET amount = amount + ?, version = version + 1 WHERE id = ?")
                updateSecondStatement.use { statement ->
                    statement.setLong(1, resultAmount)
                    statement.setLong(2, accountIdSecond)
                    statement.executeUpdate()
                }
                conn.commit()

                println("transfer from $accountId1 to $accountId2: $amount")
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
