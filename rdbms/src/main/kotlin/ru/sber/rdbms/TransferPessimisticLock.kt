package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(private val connection: Connection) {

    /**
     * Функция перевода денег с одного счета на другой.
     * Через пессимистичные блокировки. Как возможен deadlock ? Учесть его в решении
     *
     * Deadlock возможен, если происходит одновременно два процесса перевода с кошелька1 на кошелек2 и наоборот с 2 на 1.
     * Тогда могут быть заблокированы обе записи в account для обновления.
     * Решение - выполнять select for update сразу для двух кошельков
     */
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val accountPair = getAccountForUpdate(accountId1, accountId2, conn)
                if (accountPair.first.amount < amount) throw NotEnoughMoneyException()

                updateAccount(accountPair.first, "- $amount", conn)
                updateAccount(accountPair.second, "+ $amount", conn)

                conn.commit()
                println("TransferOptimisticLock: successfully transfered amount $amount from $accountId1 to $accountId2")
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }

        }
    }


    private fun getAccountForUpdate(accountId1: Long, accountId2: Long, conn: Connection): Pair<AccountEntity, AccountEntity> {
        val prepareStatement = conn.prepareStatement("select * from account where id in (?, ?) for update")

        prepareStatement.use { statement ->
            statement.setLong(1, accountId1)
            statement.setLong(2, accountId2)
            statement.executeQuery().use {
                val first: AccountEntity
                val second: AccountEntity
                it.next()
                if (it.getLong("id") == accountId1) {

                    first = AccountEntity(accountId1, it.getInt("amount"), it.getInt("version"))
                    it.next()
                    second = AccountEntity(accountId2, it.getInt("amount"), it.getInt("version"))
                    return Pair(first, second)
                } else {
                    second = AccountEntity(accountId2, it.getInt("amount"), it.getInt("version"))
                    it.next()
                    first = AccountEntity(accountId1, it.getInt("amount"), it.getInt("version"))
                    return Pair(first, second)
                }
            }
        }
    }

    private fun updateAccount(account: AccountEntity, amountOperation: String, conn: Connection) {
        val prepareStatementUpdate1 =
                conn.prepareStatement("update account set amount = amount $amountOperation where id = ?")
        prepareStatementUpdate1.use { statement ->
            statement.setLong(1, account.id)

            statement.executeUpdate()
        }
    }
}
