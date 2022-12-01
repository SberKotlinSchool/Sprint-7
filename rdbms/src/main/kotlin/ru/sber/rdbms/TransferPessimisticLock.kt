package ru.sber.rdbms

import java.sql.Connection

class TransferPessimisticLock(private val connection: Connection) {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val accounts = listOf(accountId1, accountId2).sorted()

        val selectStatement = connection.prepareStatement("select * from account where id = ? for update")
        selectStatement.use {
            accounts.forEach { id ->
                it.setLong(1, id)
                it.addBatch()
            }
            it.executeBatch()
        }

        val updateStatement = connection.prepareStatement("update account set amount = amount + ? where id = ?")
        updateStatement.use {
            accounts.forEachIndexed { index, id ->
                //val multiplier = if (index % 2 == 0) -1 else 1
                it.setLong(1, amount * (if (index % 2 == 0) -1 else 1))
                it.setLong(2, id)
                it.addBatch()
            }
            it.executeBatch()
        }
    }
}
