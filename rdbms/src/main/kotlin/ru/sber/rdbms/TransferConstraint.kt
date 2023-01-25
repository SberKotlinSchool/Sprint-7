package ru.sber.rdbms

import java.lang.IllegalStateException
import java.sql.Connection

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val updateSql = "update account1 set amount = amount + ? where id = ?"
        connection { conn ->
            conn.execUpdate(updateSql) { ps ->
                ps.setLong(1, amount)
                ps.setLong(2, accountId1)
            }
            conn.execUpdate(updateSql) { ps ->
                ps.setLong(1, amount)
                ps.setLong(2, accountId1)
            }
        }
    }
}


