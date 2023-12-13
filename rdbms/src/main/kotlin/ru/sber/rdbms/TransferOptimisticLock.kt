package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException


class TransferOptimisticLock(private val connection: Connection) {

    /**
     * Функция перевода денег с одного счета на другой.
     * Через оптимистичные блокировки.
     * В случае нехватки средств функция или нарушении целостности данных должна выкидывать кастомное исключение
     */
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false

                //Фиксируем данные на счете 1
                val account1 = getAccount(accountId1, conn)
                if (account1.amount < amount) throw NotEnoughMoneyException()

                //Фиксируем данные на счете 2
                val account2 = getAccount(accountId2, conn)

                //Уменьшаем сумму на счете 1
                updateAccount(account1, -1 *  amount, conn)

                //Увеличиваем сумму на счете 2
                updateAccount(account2, amount, conn)

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

    private fun getAccount(accountId: Long, conn: Connection): AccountEntity {
        val prepareStatement = conn.prepareStatement("select * from account where id = ?")

        prepareStatement.use { statement ->
            statement.setLong(1, accountId)
            statement.executeQuery().use {
                it.next()

                return AccountEntity(accountId, it.getInt("amount"), it.getInt("version"))

            }
        }
    }

    private fun updateAccount(accountEntity: AccountEntity, amountOperation: Long, conn: Connection) {
        val prepareStatementUpdate1 =
                conn.prepareStatement("update account set amount = amount + ?, version = version + 1 where id = ? and version = ?")
        prepareStatementUpdate1.use { statement ->
            statement.setLong(1, amountOperation)
            statement.setLong(2, accountEntity.id)
            statement.setInt(3, accountEntity.version)

            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0) throw SQLException("Concurrent update $accountEntity")
        }
    }

}
