package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

/**
CREATE TABLE accounts
(
id bigserial,
amount int,
version int
);
ALTER TABLE accounts ADD PRIMARY KEY (id);
 */
class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Int) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            conn.autoCommit = false
            var version = 0
            try {
                //take from the first account
                var prepareStatement1 = conn.prepareStatement("select * from accounts where id = $accountId1")
                prepareStatement1.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        version = it.getInt("version")
                        if (it.getInt("amount") < amount)
                            throw SQLException("Balance of your account is insufficient")
                    }
                }
                var prepareStatement2 = conn.prepareStatement("update accounts set amount = amount - $amount, version = version + 1 where id = $accountId1 and version = ?")
                prepareStatement2.use { statement ->
                    statement.setInt(1, version)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw SQLException("Concurrent update")
                }
                // add to the second account
                prepareStatement1 = conn.prepareStatement("select * from accounts where id = $accountId2")
                prepareStatement1.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        version = it.getInt("version")
                    }
                }
                prepareStatement2 = conn.prepareStatement("update accounts set amount = amount + $amount, version = version + 1 where id = $accountId2 and version = ?")
                prepareStatement2.use { statement ->
                    statement.setInt(1, version)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
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