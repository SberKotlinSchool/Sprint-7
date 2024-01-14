package ru.sber.rdbms

import liquibase.pro.packaged.it
import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    TransferOptimisticLock().transfer(2, 1, 50)
    TransferOptimisticLock().transfer(2, 1, 100000)
}

class TransferOptimisticLock {

    val connection = DriverManager.getConnection(
        JDBC_POSTGRES_DB_CONNECTION, DB_USER, DB_PASS
    )

    /** Списываем с первого счёта, и пополняем второй
     * cutAccountId: Long - списываем со счёта
     * addAccountId: Long - добавляем на счёт
     */
    fun transfer(cutAccountId: Long, addAccountId: Long, amount: Long) {

        connection.use { conn ->
            val cutAmountQuery = conn.prepareStatement(
                "update accounts set amount = amount - ?, version = version + 1 where id = ? and version = ?;"
            )

            val addAmountQuery = conn.prepareStatement(
                "update accounts set amount = amount + ?, version = version + 1 where id = ? and version = ? ;"
            )

            try {
                conn.autoCommit = false;

                checkOperationValid(cutAccountId, amount)

                cutAmountQuery.use { query ->
                    query.setLong(1, amount)
                    query.setLong(2, cutAccountId)
                    query.setLong(3, getVersion(cutAccountId))

                    val result = query.executeUpdate()
                    if (result == 0)
                        throw SQLException("Ошибка в момент списания")
                }

                addAmountQuery.use { query ->
                    query.setLong(1, amount)
                    query.setLong(2, addAccountId)
                    query.setLong(3, getVersion(addAccountId))

                    val result = query.executeUpdate()
                    if (result == 0)
                        throw SQLException("Ошибка в момент пополнения")
                }

                conn.commit()
                println("Операция проведена успешно!")
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            }
        }
    }

    private fun getVersion(accountId: Long): Long {
        val getVersionQuery = connection.prepareStatement("SELECT version FROM account1 WHERE id = ?")
        var version = 0L;
        getVersionQuery.use { query ->
            query.setLong(1, accountId)
            query.executeQuery().use {
                it.next()
                version = it.getLong("version")
            }
        }

        return version
    }

    /**
     * Проверим, что хватает средств для списания на счёте
     */
    private fun checkOperationValid(cutAccountId: Long, amount: Long) {
        val checkAmountQuery = connection.prepareStatement(
            "select amount - ? as amount from accounts where id = ?"
        )

        checkAmountQuery.use { query ->
            query.setLong(1, amount)
            query.setLong(2, cutAccountId)

            query.executeQuery().use {
                it.next()
                if (it.getLong("amount") < 0)
                    throw SQLException("Недостаточно средств для списания на счёте - ${cutAccountId}!")
            }
        }
    }
}