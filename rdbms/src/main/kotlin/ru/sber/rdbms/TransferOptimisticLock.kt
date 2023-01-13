package ru.sber.rdbms

import java.sql.Connection
import java.sql.SQLException
import java.sql.Struct

class TransferOptimisticLock(private val connection: Connection) {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatement1 = conn.prepareStatement("select * from account1 where id in (?, ?) order by id")
                var versions = IntArray(2)
                var sums = LongArray(2)
                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeQuery().use {
                        var i = 0
                        while (it.next()) {
                            versions[i] = it.getInt("version")
                            sums[i] = it.getLong("amount")
                            i++
                        }
                    }
                }

                if (sums[0] < amount){
                    throw SQLException("There are not enough funds in the account")
                }

                val prepareStatement2 =
                    conn.prepareStatement("update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")
                prepareStatement2.use { statement ->
                    statement.setLong(1, -amount)
                    statement.setLong(2, accountId1)
                    statement.setInt(3, versions[0])
                    statement.addBatch()
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setInt(3, versions[1])
                    statement.addBatch()
                    val updatedRows = statement.executeBatch()
                    if (updatedRows.isEmpty())
                        throw SQLException("Concurrent update")
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
