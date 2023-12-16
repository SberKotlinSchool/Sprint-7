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

                val preparedStatementForSelectWithBlocking = connection.prepareStatement("select * from account1 where id IN (?, ?) for update")
                preparedStatementForSelectWithBlocking.setLong(1, accountId1)
                preparedStatementForSelectWithBlocking.setLong(2, accountId2)


                preparedStatementForSelectWithBlocking.executeQuery().use { resultSet ->

                    while (resultSet.next()) {
                        val id = resultSet.getLong("id")
                        if (id == accountId1) {
                            val amountForDebit = resultSet.getLong("amount")
                            if ((amountForDebit - amount) < 0) {
                                throw AmountException()
                            }
                        }
                    }
                }

                val preparedStatementForUpdate = connection.prepareStatement("update account1 set amount = amount + ? where id = ?")

                preparedStatementForUpdate.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, accountId2)

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
