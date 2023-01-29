package ru.sber.rdbms

import java.sql.SQLException

class TransferPessimisticLock(private val connectionManager : ConnectionManager)  {

    fun transfer(sourceAccountId: Long, targetAccountId: Long, amount: Int) {
        val conn = connectionManager.getConnection()
        val autoCommit = conn.autoCommit
        try {
            conn.autoCommit = false
            checkBalanceInTransaction(sourceAccountId, amount, conn, true)
            blockingTargetAccount(targetAccountId, conn)
            transferInTransaction(sourceAccountId, targetAccountId, amount, conn)
            conn.commit()
        } catch (exception: SQLException) {
            println(exception.message)
            conn.rollback()
        } finally {
            conn.autoCommit = autoCommit
        }
    }
}
