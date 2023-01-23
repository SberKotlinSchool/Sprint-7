package ru.sber.rdbms.constraints

import ru.sber.rdbms.DB_PASS
import ru.sber.rdbms.DB_URL
import ru.sber.rdbms.DB_USER
import ru.sber.rdbms.exceptions.UnsuccessfulTransactionException
import java.sql.DriverManager

private const val SQL_UPDATE_QUERY = "update account set amount = amount + ? where id = ?"
private const val SQL_AMOUNT_INDEX = 1
private const val SQL_ACCOUNT_ID_INDEX = 2

/**
 * Basic atomic increment with table constraint.
 */
class TransferConstraint {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(DB_URL, DB_USER, DB_PASS).use { connection ->
            val isAutoCommitEnabled = connection.autoCommit
            runCatching {
                connection.autoCommit = false

                val affectedRecords = connection.prepareStatement(SQL_UPDATE_QUERY).use { statement ->
                    statement.run {
                        setLong(SQL_AMOUNT_INDEX, -amount)
                        setLong(SQL_ACCOUNT_ID_INDEX, accountId1)
                        addBatch()

                        setLong(SQL_AMOUNT_INDEX, amount)
                        setLong(SQL_ACCOUNT_ID_INDEX, accountId2)
                        addBatch()

                        executeBatch().sum()
                    }
                }

                if (affectedRecords != 2) {
                    throw UnsuccessfulTransactionException()
                }
                connection.commit()
            }.onFailure {
                println(it.message)
                connection.rollback()
            }
            connection.autoCommit = isAutoCommitEnabled
        }
    }
}
