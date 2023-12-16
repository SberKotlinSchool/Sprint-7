package ru.sber.rdbms

import ru.sber.rdbms.exceptions.AmountException
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatement1 = conn.prepareStatement("select * from account1 where id = ? for update")
                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)

                    statement.executeQuery().use {
                        val amountForDebit = it.getLong("amount")
                        if ((amountForDebit - amount) < 0) {
                            throw AmountException();
                        }
                    }
                }

                val prepareStatement3 = conn.prepareStatement("select * from account1 where id = ? for update")
                prepareStatement3.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery()
                }

                val prepareStatement2 = conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                prepareStatement2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)

                    statement.executeUpdate()
                }

                val prepareStatement4 = conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                prepareStatement4.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
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
}
