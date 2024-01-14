package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    TransferPessimistickLock().transfer(2, 1, 50)
    TransferPessimistickLock().transfer(2, 1, 11150)
}

class TransferPessimistickLock {

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
                "update accounts set amount = amount - ? where id = ?;"
            )

            val addAmountQuery = conn.prepareStatement(
                "update accounts set amount = amount + ? where id = ?;"
            )

            try {
                conn.autoCommit = false;

                val selectStatement = conn.prepareStatement("select * from accounts where id in (?, ?) for update")
                selectStatement.use { statement ->
                    statement.setLong(1, cutAccountId)
                    statement.setLong(2, addAccountId)
                    statement.executeQuery().use {
                        while (it.next()) {
                            if (it.getLong("id") == cutAccountId) {
                                val amountBefore = it.getLong("amount")
                                if (amountBefore < amount) {
                                    throw SQLException("Недостаточно средст на счёте!")
                                }
                            }
                        }
                    }
                }

                cutAmountQuery.use { _ ->
                    cutAmountQuery.setLong(1, amount)
                    cutAmountQuery.setLong(2, cutAccountId)

                    val result = cutAmountQuery.executeUpdate()
                    if (result == 0)
                        throw SQLException("Ошибка в момент списания")
                }

                addAmountQuery.use { query ->
                    query.setLong(1, amount)
                    query.setLong(2, addAccountId)

                    val result = query.executeUpdate()
                    if (result == 0)
                        throw SQLException("Ошибка в момент пополнения")
                }

                conn.commit()
                println("Операция проведена успешно!")
//            } catch (exception: Exception) {
//                exception.printStackTrace()
//                conn.rollback()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
            }
        }
    }
}