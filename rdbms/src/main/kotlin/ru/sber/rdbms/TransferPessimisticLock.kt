package ru.sber.rdbms

import ru.sber.rdbms.TransferUtils.Companion.getAccInfoForUpdate
import ru.sber.rdbms.TransferUtils.Companion.throwOverdraftException

class TransferPessimisticLock {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        var accountId1st = accountId1
        var accountId2nd = accountId2
        var amountX = amount

        // deadlock protection - sort resources for lock by account id
        if (accountId1 > accountId2) {
            accountId1st = accountId2
            accountId2nd = accountId1
            amountX = -amount
        }

        TransferUtils.createConnection().use { conn ->

            println("----- Transfer sum $amount from account $accountId1 to account $accountId2 -----")
            val autoCommit = conn.autoCommit
            conn.autoCommit = false

            try {
                getAccInfoForUpdate(conn, accountId1st).let { accInfo ->
                    if (accInfo.version == 0) {
                        throw GetAccountInfoException("Unable to lock account $accountId1st")
                    } else if (amountX > 0 && accInfo.amount < amount) {
                        throwOverdraftException(accountId1, accountId2, amount)
                    }
                }
                getAccInfoForUpdate(conn, accountId2nd).let { accInfo ->
                    if (accInfo.version == 0) {
                        throw GetAccountInfoException("Unable to lock account $accountId2nd")
                    } else if (amountX < 0 && accInfo.amount < amount) {
                        throwOverdraftException(accountId1, accountId2, amount)
                    }
                }

                conn.prepareStatement("update account1 set amount = amount - (?), version = version + 1 where id = ?")
                    .use {
                        it.setInt(1, amountX.toInt())
                        it.setLong(2, accountId1st)
                        it.executeUpdate()
                        println("1st update executed")
                    }
                conn.prepareStatement("update account1 set amount = amount + (?), version = version + 1 where id = ?")
                    .use {
                        it.setInt(1, amountX.toInt())
                        it.setLong(2, accountId2nd)
                        it.executeUpdate()
                        println("2nd update executed")
                    }
                conn.commit()
                println("Transfer committed")

            } catch (e: Exception) {
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
