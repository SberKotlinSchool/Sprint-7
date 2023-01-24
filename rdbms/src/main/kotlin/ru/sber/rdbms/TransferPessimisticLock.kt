package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException

class TransferPessimisticLock(private val connection: Connection)  {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatement1 = conn.prepareStatement("select * from account1 where id in (?, ?) order by id for update")
                var sums = LongArray(2)
                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeQuery().use {
                        var i = 0
                        while (it.next()) {
                            sums[i] = it.getLong("amount")
                            i++
                        }
                    }
                }

                if (sums[0] < amount){
                    throw SQLException("There are not enough funds in the account")
                }

                val prepareStatement2 = conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                prepareStatement2.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.addBatch()
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.addBatch()
                    statement.executeBatch()
                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
