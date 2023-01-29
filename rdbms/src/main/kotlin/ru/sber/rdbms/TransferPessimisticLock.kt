package ru.sber.rdbms

import java.lang.IllegalStateException
import java.sql.Connection

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection { conn ->
            var acc1Amount = getAccountInfo(conn, accountId1)
            acc1Amount -= amount
            var acc2Amount = getAccountInfo(conn, accountId2)
            acc2Amount += amount
            if (acc1Amount < 0) {
                throw IllegalStateException("Недостаточно средства на счете списания")
            }
            updateAccAmount(conn, accountId1, acc1Amount)
            updateAccAmount(conn, accountId2, acc2Amount)
        }
    }
}


private fun updateAccAmount(conn: Connection, accountId: Long, amount: Long) {
    conn.execUpdate("update account1 set amount = amount + ? where id = ?") {
        it.setLong(1, amount)
        it.setLong(2, accountId)
    }.let {
        if (it == 0)
            throw IllegalStateException("Concurrent update")
    }
}

private fun getAccountInfo(conn: Connection, accountId1: Long) =
    conn.query("select * from account1 where id = ? for update", { st ->
        st.setLong(1, accountId1)
    }
    ) {
        it.getLong("amount")
    }