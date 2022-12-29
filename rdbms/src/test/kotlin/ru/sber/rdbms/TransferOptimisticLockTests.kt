package ru.sber.rdbms

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.sber.rdbms.exception.NegativeBalanceException

class TransferOptimisticLockTests {
    private var transferOptimisticLock = TransferOptimisticLock()

    @Test
    fun `should successful execute query`() {
        transferOptimisticLock.transfer(4, 2, 10)
    }

    @Test
    fun `should throw exception`() {
        Assertions.assertThrows(NegativeBalanceException::class.java) {
            transferOptimisticLock.transfer(1, 3, 10000)
        }
    }
}