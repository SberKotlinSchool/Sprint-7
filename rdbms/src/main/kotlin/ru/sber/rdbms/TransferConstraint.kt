package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferConstraint(
        private val connection: Connection) {

    /**
     * Функция перевода денег с одного счета на другой
     * через атомарный инкремент/декримент суммы на счетах(update ... set x=x+100)
     * и ограничение(integrity constraint) на таблице
     */
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val statement = conn.prepareStatement(
                        """
                        update account set amount = amount - ? where id = ?;
                        update account set amount = amount + ? where id = ?;
                    """.trimIndent()
                )
                statement.setLong(1, amount)
                statement.setLong(2, accountId1)
                statement.setLong(3, amount)
                statement.setLong(4, accountId2)

                statement.executeUpdate()
                conn.commit()


                println("TransferConstraint: successfully transfered amount $amount from $accountId1 to $accountId2")
            } catch (e: SQLException) {
                conn.rollback()
                println("transaction rollbacked")
            } finally {
                conn.autoCommit = autoCommit
            }

        }

    }
}
