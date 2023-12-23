package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {

    TransferPessimisticLock().transfer(2, 1, 800)
}
class TransferPessimisticLock {

    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            var amountAccount1 = 0L

            try {

                if ( accountId1 ==  accountId2)
                    throw SQLException("Прыжок на месте")

                conn.autoCommit = false

                listOf(accountId1, accountId2).sorted().forEach{ accountId ->

                    val statSelectAccount = conn.prepareStatement("select * from account1 where id = ? for update;")
                    statSelectAccount.use { statement ->
                        statement.setLong(1, accountId)
                        statement.executeQuery().use {
                            it.next()
                            println(it.getLong("id"))
                            println(it.getLong("amount"))
                            if (accountId1 == accountId)
                            amountAccount1 = it.getLong("amount")
                        }
                    }

                }

                if (amountAccount1 - amount < 0)
                    throw SQLException("Недостаточно денег на счете")

                val statUpdate1 = conn.prepareStatement(
                    "update account1 set amount = amount - ? where id = ?"  )
                statUpdate1.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }

                val statUpdate2 = conn.prepareStatement(
                    "update account1 set amount = amount + ? where id = ?"  )
                statUpdate2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()

                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
