package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
     val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "s3cr3t"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            try {
                conn.autoCommit = false
                val amount1 = getAmount(conn, accountId1)
                if (amount1 - amount < 0) throw SQLException("Недостаточно средств")

                getAmount(conn, accountId2)

                update(conn, -amount, accountId1)
                update(conn, amount, accountId2)

                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }

    fun update(connection: Connection, amount: Long, accountId1: Long) {
        connection.prepareStatement("update account1 set amount = amount + ?  where id = ?")
            .use { statement ->
                statement.setLong(1, amount)
                statement.setLong(2, accountId1)
                statement.executeUpdate()
        }
    }

    fun getAmount(connection: Connection, accountId1: Long): Long {
        connection.prepareStatement("select * from account1 where id = ? for update")
            .use { statement ->
                statement.setLong(1, accountId1)
                statement.executeQuery().use {
                    it.next()
                    return it.getLong("amount")
            }
        }
    }
}

fun main(){
    /// поленился чреез тесты, сори
    val tr = TransferPessimisticLock()
    tr.connection.use { conn ->
        val amount1 = tr.getAmount(conn, 1)
        val amount2 = tr.getAmount(conn, 2)
        TransferPessimisticLock().transfer(1, 2, 1000)

        assert(amount1 == tr.getAmount(conn, 1))
        assert(amount2 == tr.getAmount(conn, 2))
    }
}
