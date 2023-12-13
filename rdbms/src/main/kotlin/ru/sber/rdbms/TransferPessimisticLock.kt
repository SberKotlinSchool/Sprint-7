package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(private val connection: Connection) {

    /**
     * Функция перевода денег с одного счета на другой.
     * Через пессимистичные блокировки.
     *
     * Как возможен deadlock ? Дедлок возможен, поскольку захват строки в запросе select for update делается построчно.
     * Если два процесса одновременно делают переводы между кошельками 1 - 2 и  2 - 1, и выбор строк не детерминирован,
     * то может возникнуть ситуация, когда select for update заблокирует по одной разной строке, и будет ожидать другую уже заблокированную.
     * Решение - детерминированная последовательность блокировок строк. Т.е. одинаковый порядок.
     */
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                val accountPair = getAccountForUpdate(accountId1, accountId2, conn)
                if (accountPair.first.amount < amount) throw NotEnoughMoneyException()

                val prepareStatementUpdate =
                        conn.prepareStatement("update account set amount = amount + ? where id = ?")
                prepareStatementUpdate.use { statement ->
                    statement.setLong(1, -1 * amount)
                    statement.setLong(2, accountPair.first.id)
                    statement.addBatch()

                    statement.setLong(1, amount)
                    statement.setLong(2, accountPair.second.id)
                    statement.addBatch()

                    statement.executeBatch()
                }

                conn.commit()
                println("TransferPessimisticLock: successfully transfered amount $amount from $accountId1 to $accountId2")
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }

        }
    }


    /**
     * Блокировка строк будет выполнена в одном и том же порядке.
     * Для случая одновременных переводов 1-2 и 2-1
     */
    private fun getAccountForUpdate(accountId1: Long, accountId2: Long, conn: Connection): Pair<AccountEntity, AccountEntity> {
        val prepareStatement = conn.prepareStatement("select * from account where id in (?, ?) order by id for update")

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

}
