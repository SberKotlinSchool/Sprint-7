package ru.sber.rdbms

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TransferPessimisticLockTest: BaseTest() {

    @Test
    fun transfer() {
        val connection1 = getConnection()
        val connection2 = getConnection()

        connection1.use { conn ->
            val amount1 = getAmount(conn, 1)
            val amount2 = getAmount(conn, 2)

            TransferPessimisticLock(connection2).transfer(1, 2, 100)

            assertEquals( amount1 - 100, getAmount(conn,1))
            assertEquals( amount2 + 100, getAmount(conn,2))
        }
    }
}