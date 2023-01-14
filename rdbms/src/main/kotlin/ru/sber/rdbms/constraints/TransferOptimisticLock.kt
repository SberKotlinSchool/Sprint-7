package ru.sber.rdbms.constraints

import ru.sber.rdbms.DB_PASS
import ru.sber.rdbms.DB_URL
import ru.sber.rdbms.DB_USER
import ru.sber.rdbms.exceptions.InsufficientFundsException
import ru.sber.rdbms.exceptions.UnsuccessfulTransactionException
import java.sql.DriverManager

private const val SQL_COLUMN_NAME_AMOUNT = "amount"
private const val SQL_COLUMN_NAME_VERSION = "version"

private const val SQL_SELECT_QUERY = "select * from account where id = ?"
private const val SQL_SELECT_ACCOUNT_ID_INDEX = 1

private val SQL_UPDATE_QUERY = """
    update account 
    set amount = ?, version = version + 1 
    where id = ? and version = ?
""".trimIndent()
private const val SQL_UPDATE_AMOUNT_INDEX = 1
private const val SQL_UPDATE_ACCOUNT_ID_INDEX = 2
private const val SQL_UPDATE_VERSION_INDEX = 3

class TransferOptimisticLock {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        DriverManager.getConnection(DB_URL, DB_USER, DB_PASS).use { connection ->
            val isAutoCommitEnabled = connection.autoCommit

            val idList = listOf(accountId1, accountId2)
            runCatching {
                connection.autoCommit = false

                val (account1Info, account2Info) = connection.prepareStatement(SQL_SELECT_QUERY).use { statement ->
                    statement.run {
                        idList.map { id ->
                            setLong(SQL_SELECT_ACCOUNT_ID_INDEX, id)

                            executeQuery().use { result ->
                                result.next()
                                result.getInt(SQL_COLUMN_NAME_AMOUNT) to result.getInt(SQL_COLUMN_NAME_VERSION)
                            }
                        }
                    }
                }

                val affectedRecords = connection.prepareStatement(SQL_UPDATE_QUERY).use { statement ->
                    val (account1Amount, account1Version) = account1Info

                    if (account1Amount - amount < 0) {
                        throw InsufficientFundsException(accountId1)
                    }

                    statement.run {
                        setLong(SQL_UPDATE_AMOUNT_INDEX, account1Amount - amount)
                        setLong(SQL_UPDATE_ACCOUNT_ID_INDEX, accountId1)
                        setInt(SQL_UPDATE_VERSION_INDEX, account1Version)
                        addBatch()

                        val (account2Amount, account2Version) = account2Info

                        setLong(SQL_UPDATE_AMOUNT_INDEX, account2Amount + amount)
                        setLong(SQL_UPDATE_ACCOUNT_ID_INDEX, accountId2)
                        setInt(SQL_UPDATE_VERSION_INDEX, account2Version)
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
