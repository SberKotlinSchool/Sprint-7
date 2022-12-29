package ru.sber.rdbms

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import ru.sber.rdbms.exception.NegativeBalanceException


class TransferPessimisticLockTests {
    private var transferPessimisticLockTest = TransferPessimisticLock()

    @Test
    fun `should successful execute query`() {
        transferPessimisticLockTest.transfer(4, 2, 10)
    }

    @Test
    fun `should throw exception`() {
        assertThrows(NegativeBalanceException::class.java) {
            transferPessimisticLockTest.transfer(1, 3, 10000)
        }
    }
}