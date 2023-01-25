package ru.sber.rdbms

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.postgresql.util.PSQLException

internal class TransferOptimisticLockTest {

    lateinit var transferOptimisticLock: TransferOptimisticLock

    @BeforeEach
    fun setUp() {
        TransferTestUtils.initAccounts(1000, 1500)
        transferOptimisticLock = TransferOptimisticLock()
    }

    @Test
    fun transferNormalTest() {
        assertDoesNotThrow { transferOptimisticLock.transfer(1, 2, 500) }
    }

    @Test
    fun transferOverdraftTest() {
        assertThrows(OverdraftException::class.java) { transferOptimisticLock.transfer(1, 2, 1001) }
    }

    @Test
    fun transferReversedNormalTest() {
        assertDoesNotThrow { transferOptimisticLock.transfer(2, 1, 1001) }
    }

    @Test
    fun transferReversedOverdraftTest() {
        assertThrows(OverdraftException::class.java) { transferOptimisticLock.transfer(2, 1, 1501) }
    }
}