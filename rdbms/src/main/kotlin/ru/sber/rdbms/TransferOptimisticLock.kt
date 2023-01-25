package ru.sber.rdbms

import java.lang.IllegalStateException
import java.sql.Connection

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection { conn ->
            var (acc1Amount, acc1Version) = getAccountInfo(conn, accountId1)
            acc1Amount -= amount
            var (acc2Amount, acc2Version) = getAccountInfo(conn, accountId1)
            acc2Amount += amount
            if (acc1Amount < 0) {
                throw IllegalStateException("Недостаточно средства на счете списания")
            }
            updateAccAmount(conn, accountId1, acc1Amount, acc1Version)
            updateAccAmount(conn, accountId2, acc2Amount, acc2Version)
        }
    }
}


private fun updateAccAmount(conn: Connection, accountId: Long, amount: Long, version: Int) {
    conn.execUpdate("update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?") {
        it.setLong(1, amount)
        it.setLong(2, accountId)
        it.setInt(3, version)
    }.let {
        if (it == 0)
            throw IllegalStateException("Concurrent update")
    }
}

private fun getAccountInfo(conn: Connection, accountId1: Long): Pair<Long, Int> =
    conn.query("select * from account1 where id = ?", { st ->
            st.setLong(1, accountId1)
        }
    ) {
        it.getLong("amount") to it.getInt("version")
    }