package ru.sber.rdbms

import ru.sber.rdbms.exception.CustomException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TransferOptimisticLock {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "s3cr3t"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { conn ->
            try {
                conn.autoCommit = false
                val (amount1, version1) = getAccountInfo(conn, accountId1)
                if (amount1 - amount < 0) throw CustomException("Недостаточно средств")

                val (amount2, version2) = getAccountInfo(conn, accountId2)

                executeUpdate(conn, accountId1, -amount, version1)
                executeUpdate(conn, accountId2, amount, version2)

                conn.commit()
            } catch (exception: Exception) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = true
            }
        }
    }


    private fun executeUpdate(connection: Connection, accountId: Long, amount: Long, version: Int) {

        connection.prepareStatement("update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")
            .use { statement ->
            statement.setLong(1, amount)
            statement.setLong(2, accountId)
            statement.setInt(3, version)
            val updatedRows = statement.executeUpdate()
            if (updatedRows == 0)
                throw CustomException("Concurrent update")
        }
    }

    fun getAccountInfo(connection: Connection, accountId1: Long): Pair<Long, Int> {
        val prepareStatement1 = connection.prepareStatement("select * from account1 where id = ?")
        prepareStatement1.use { statement ->
            statement.setLong(1, accountId1)
            statement.executeQuery().use {
                it.next()
                return it.getLong("amount") to it.getInt("version")
            }
        }
    }
}

fun main(){
    val tr = TransferOptimisticLock()
    tr.connection.use { conn ->
        val amount1 = tr.getAccountInfo(conn, 1)
        val amount2 = tr.getAccountInfo(conn, 2)
        TransferPessimisticLock().transfer(1, 2, 1000)

        assert(amount1 == tr.getAccountInfo(conn, 1))
        assert(amount2 == tr.getAccountInfo(conn, 2))
    }
}