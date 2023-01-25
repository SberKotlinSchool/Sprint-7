package ru.sber.rdbms

import ru.sber.rdbms.TransferUtils.Companion.getAccInfo
import ru.sber.rdbms.TransferUtils.Companion.throwOverdraftException
import java.sql.SQLException

class TransferOptimisticLock {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        TransferUtils.createConnection().use { conn ->

            println("----- Transfer sum $amount from account $accountId1 to account $accountId2 -----")
            val autoCommit = conn.autoCommit
            conn.autoCommit = false

            try {
                val acc1Info = getAccInfo(conn, accountId1)

                if (acc1Info.version == 0) {
                    throw GetAccountInfoException("Unable to get info of account $accountId1")
                } else if (acc1Info.amount < amount) {
                    throwOverdraftException(accountId1, accountId2, amount)
                }
                conn.prepareStatement("update account1 set amount = amount - (?), version = version + 1 where id = ? and version = ?")
                    .use {
                        it.setInt(1, amount.toInt())
                        it.setLong(2, accountId1)
                        it.setInt(3, acc1Info.version)

                        if (it.executeUpdate() == 0) {
                            throw SQLException("Concurrent update")
                        }
                        println("1st update executed")
                    }

                val acc2Info = getAccInfo(conn, accountId2)

                if (acc2Info.version == 0) {
                    throw GetAccountInfoException("Unable to get info of account $accountId2")
                }
                conn.prepareStatement("update account1 set amount = amount + (?), version = version + 1 where id = ? and version = ?")
                    .use {
                        it.setInt(1, amount.toInt())
                        it.setLong(2, accountId2)
                        it.setInt(3, acc2Info.version)

                        if (it.executeUpdate() == 0) {
                            throw SQLException("Concurrent update")
                        }
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
