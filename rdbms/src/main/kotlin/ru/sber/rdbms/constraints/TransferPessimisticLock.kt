package ru.sber.rdbms.constraints

import ru.sber.rdbms.DB_PASS
import ru.sber.rdbms.DB_URL
import ru.sber.rdbms.DB_USER
import ru.sber.rdbms.exceptions.InsufficientFundsException
import ru.sber.rdbms.exceptions.UnsuccessfulTransactionException
import java.sql.DriverManager

private const val SQL_COLUMN_NAME_AMOUNT = "amount"

private const val SQL_SELECT_QUERY = "select * from account where id in (?, ?) for update"
private const val SQL_SELECT_FIRST_ACCOUNT_ID_INDEX = 1
private const val SQL_SELECT_SECOND_ACCOUNT_ID_INDEX = 2

private const val SQL_UPDATE_QUERY = "update account set amount = ? where id = ?"
private const val SQL_UPDATE_AMOUNT_INDEX = 1
private const val SQL_UPDATE_ACCOUNT_ID_INDEX = 2

class TransferPessimisticLock {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(DB_URL, DB_USER, DB_PASS).use { connection ->
            val isAutoCommitEnabled = connection.autoCommit

            val idList = listOf(accountId1, accountId2)
            runCatching {
                connection.autoCommit = false

                val (account1Amount, account2Amount) = connection.prepareStatement(SQL_SELECT_QUERY).use { statement ->
                    statement.run {
                        setLong(SQL_SELECT_FIRST_ACCOUNT_ID_INDEX, accountId1)
                        setLong(SQL_SELECT_SECOND_ACCOUNT_ID_INDEX, accountId2)

                        executeQuery().use { result ->
                            idList.map {
                                result.next()
                                result.getInt(SQL_COLUMN_NAME_AMOUNT)
                            }
                        }
                    }
                }

                val affectedRecords = connection.prepareStatement(SQL_UPDATE_QUERY).use { statement ->
                    if (account1Amount - amount < 0) {
                        throw InsufficientFundsException(accountId1)
                    }

                    statement.run {
                        setLong(SQL_UPDATE_AMOUNT_INDEX, account1Amount - amount)
                        setLong(SQL_UPDATE_ACCOUNT_ID_INDEX, accountId1)
                        addBatch()

                        setLong(SQL_UPDATE_AMOUNT_INDEX, account2Amount + amount)
                        setLong(SQL_UPDATE_ACCOUNT_ID_INDEX, accountId2)
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
