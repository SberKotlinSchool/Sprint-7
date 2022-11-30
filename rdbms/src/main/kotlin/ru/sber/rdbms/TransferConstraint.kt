package ru.sber.rdbms

import java.sql.SQLException

class TransferConstraint(private val connectionManager: ConnectionManager) {


    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {

        connectionManager.getConnection().use { conn ->
            try {
                conn.autoCommit = false
                val prepareStatement = conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                prepareStatement.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.execute()
                }

                val prepareStatement2 = conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                prepareStatement2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.execute()
                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }
}
