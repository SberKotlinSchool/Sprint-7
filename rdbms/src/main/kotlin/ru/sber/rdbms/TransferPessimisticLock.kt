package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {

    private val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        val srcAcc = Account(-1L,0L,0L)
        val trgAcc = Account(-1L,0L,0L)

        connection.use { conn -> conn.autoCommit = false

            try {
                // лучше всего это сделать через хранимку, но... проверку на положительный баланс надо делать в коде
                val selStmt = conn.prepareStatement("select id, amount, version from account1 where id = ANY(?) for update") // обходим потенциальный deadlock

                selStmt.use { statement ->
                    val sqlArray = conn.createArrayOf("LONG", arrayOf(longArrayOf(accountId1, accountId2)))
                    statement.setArray(1, sqlArray)

                    statement.executeQuery().use {
                        while (it.next()) {
                            when(it.getLong("id")) {
                                accountId1 -> {
                                    srcAcc.id = it.getLong("id")
                                    srcAcc.amount = it.getLong("amount")
                                    srcAcc.version = it.getLong("version")
                                }
                                accountId2 -> {
                                    trgAcc.id = it.getLong("id")
                                    trgAcc.amount = it.getLong("amount")
                                    trgAcc.version = it.getLong("version")
                                }
                            }
                        }
                    }

                    if ((srcAcc.id != -1L) && (trgAcc.id != -1L)) {

                        if (amount > srcAcc.amount)
                            throw java.lang.Exception("There are not enough funds on account ${srcAcc.id} to transfer")

                        val updStmt = conn.prepareStatement(
                        """
                            update account1 set amount = amount - ? where id = ?;
                            update account1 set amount = amount + ? where id = ?;
                            """
                        )
                        updStmt.use {
                            it.setLong(1,amount)
                            it.setLong(2,accountId1)
                            it.setLong(3,amount)
                            it.setLong(4,accountId2)

                            updStmt.executeUpdate()
                        }
                    }

                }
                conn.commit()

                println("Transfer $amount from account $accountId1 to account $accountId2 successfully completed.")

            } catch (exception: SQLException) {
                println(exception)
                conn.rollback()
            }
        }


    }
}
