package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    TransferConstraint().transfer(2, 1, 50)
}

class TransferConstraint {

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

                cutAmountQuery.use { _ ->
                    cutAmountQuery.setLong(1, amount)
                    cutAmountQuery.setLong(2, cutAccountId)

                    val result = cutAmountQuery.executeUpdate()
                    if (result == 0)
                        throw SQLException("Ошибка в момент списания")
                }

                addAmountQuery.use { _ ->
                    addAmountQuery.setLong(1, amount)
                    addAmountQuery.setLong(2, addAccountId)

                    val result = addAmountQuery.executeUpdate()
                    if (result == 0)
                        throw SQLException("Ошибка в момент пополнения")
                }

                conn.commit()
                println("Операция проведена успешно!")
            } catch (exception: Exception) {
                exception.printStackTrace()
                conn.rollback()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                println("Операция завершена!")
            }
        }
    }
}