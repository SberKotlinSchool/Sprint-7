package ru.sber.rdbms

import ru.sber.rdbms.TransferUtils.Companion.createConnection
import java.sql.SQLException

class TransferConstraint {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        createConnection().use { conn ->

            println("----- Transfer sum $amount from account $accountId1 to account $accountId2 -----")
            val autoCommit = conn.autoCommit
            conn.autoCommit = false

            try {
                conn.prepareStatement("update account1 set amount = amount - (?), version = version + 1 where id = ?")
                    .use {
                        it.setInt(1, amount.toInt())
                        it.setLong(2, accountId1)
                        it.executeUpdate()
                        println("1st update executed")
                    }
                conn.prepareStatement("update account1 set amount = amount + (?), version = version + 1 where id = ?")
                    .use {
                        it.setInt(1, amount.toInt())
                        it.setLong(2, accountId2)
                        it.executeUpdate()
                        println("2nd update executed")
                    }
                conn.commit()
                println("Transfer committed")

            } catch (e: SQLException) {
                conn.rollback()
                println("Transfer cancelled")
                println(e.message)
                throw e
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
