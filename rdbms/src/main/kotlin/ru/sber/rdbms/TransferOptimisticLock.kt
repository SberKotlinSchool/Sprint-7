package ru.sber.rdbms

import java.sql.SQLException

class TransferOptimisticLock(private val connectionManager : ConnectionManager) {

    fun transfer(sourceAccountId: Long, targetAccountId: Long, amount: Int) {
        val conn = connectionManager.getConnection()
        val autoCommit = conn.autoCommit
        try {
            conn.autoCommit = false
            checkBalanceInTransaction(sourceAccountId, amount, conn, false)
            val updatedRowsQnt = transferOptimisticBlockingInTransaction(sourceAccountId, targetAccountId, amount, conn)
            if (updatedRowsQnt != 1) {
                throw SQLException("Данные были изменены другой транзакцией!")
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
