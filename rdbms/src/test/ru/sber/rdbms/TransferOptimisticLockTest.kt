package ru.sber.rdbms

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager

internal class TransferOptimisticLockTest{
    private fun getConnection(): Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "postgres"
    )

    private fun getAmount(conn: Connection, id: Long): Long {

        val statement = conn.prepareStatement("select amount from account1  where id = ?")
        statement.use { stat ->
            stat.setLong(1, id)
            val resultSet = stat.executeQuery()
            resultSet.use {
                it.next()
                return it.getLong("amount")
            }
        }
    }


    @Test
    fun transferSuccess() {
        val connection = getConnection()

        connection.use { conn ->
            val amount1 = getAmount(conn, 0)
            val amount2 = getAmount(conn, 1)

            TransferOptimisticLock().transfer(0, 1, 100)

            assertEquals(amount1 - 100, getAmount(conn, 0))
            assertEquals(amount2 + 100, getAmount(conn, 1))
        }
    }

    @Test
    fun transferThrowException() {
        val connection = getConnection()

        connection.use { conn ->
            val amount1 = getAmount(conn, 0)
            val amount2 = getAmount(conn, 1)
            TransferOptimisticLock().transfer(0, 1, 10000000)

            assertEquals(amount1, getAmount(conn, 0))
            assertEquals(amount2, getAmount(conn, 1))
        }
    }

}