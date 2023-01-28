package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

/**
CREATE TABLE accounts
(
id bigserial,
amount int
);
ALTER TABLE accounts ADD PRIMARY KEY (id);
 */
class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            conn.autoCommit = false
            try {
                //take from the first account
                var prepareStatement1 = conn.prepareStatement("select * from accounts where id = $accountId1 for no key update")
                prepareStatement1.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        if (it.getInt("amount") < amount)
                            throw SQLException("Balance of your account is insufficient")
                    }
                }
                var prepareStatement2 = conn.prepareStatement("update accounts set amount = amount - $amount where id = $accountId1")
                prepareStatement2.use { statement ->
                    statement.executeUpdate()
                }
                // add to the second account
                prepareStatement1 = conn.prepareStatement("select * from accounts where id = $accountId2 for no key update")
                prepareStatement1.use { statement ->
                    statement.executeQuery()
                }
                prepareStatement2 = conn.prepareStatement("update accounts set amount = amount + $amount where id = $accountId2")
                prepareStatement2.use { statement ->
                    statement.executeUpdate()
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