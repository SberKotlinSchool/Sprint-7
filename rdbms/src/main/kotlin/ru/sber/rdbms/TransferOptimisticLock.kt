package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {

    private val dbConnection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        dbConnection.use { connection ->
            val autoCommit = connection.autoCommit
            connection.autoCommit = false

            try {
                var ver1: Long
                var ver2: Long
                val statementGetAcc1 = connection.prepareStatement("select * from account where id = ?")

                statementGetAcc1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use { resultSet ->
                        resultSet.next()
                        resultSet.getLong("amount")
                            .let { if (it - amount < 0) throw SQLException("Баланс счета не может быть меньше нуля") }
                        ver1= resultSet.getLong("version")
                    }
                }

                val statementGetAcc2 = connection.prepareStatement("select * from sccount where id = ?")
                statementGetAcc2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery().use { resultSet ->
                        resultSet.next()
                        ver2 = resultSet.getLong("version")
                    }
                }

                val statementUpdateAccounts = connection.prepareStatement("update account set amount = amount + ?, version = version + 1 where id = ? and version = ?")
                statementUpdateAccounts.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.setLong(3, ver1)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setLong(3, ver2)
                    statement.addBatch()

                    val executeBatch = statement.executeBatch()
                    if (executeBatch.isEmpty()) {
                        throw SQLException("Ошибка обновления")
                    }
                }
                connection.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                connection.rollback()
            } finally {
                connection.autoCommit = autoCommit
            }
        }
    }
}
