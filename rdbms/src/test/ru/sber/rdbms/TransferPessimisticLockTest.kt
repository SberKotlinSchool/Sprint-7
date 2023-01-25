package ru.sber.rdbms

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TransferPessimisticLockTest {

    lateinit var transferPessimisticLock: TransferPessimisticLock

    @BeforeEach
    fun setUp() {
        TransferTestUtils.initAccounts(1000, 1500)
        transferPessimisticLock = TransferPessimisticLock()
    }


    @Test
    fun transferNormalTest() {
        assertDoesNotThrow { transferPessimisticLock.transfer(1, 2, 500) }
    }

    @Test
    fun transferOverdraftTest() {
        assertThrows(OverdraftException::class.java) { transferPessimisticLock.transfer(1, 2, 1001) }
    }

    @Test
    fun transferReversedNormalTest() {
        assertDoesNotThrow { transferPessimisticLock.transfer(2, 1, 1001) }
    }

    @Test
    fun transferReversedOverdraftTest() {
        assertThrows(OverdraftException::class.java) { transferPessimisticLock.transfer(2, 1, 1501) }
    }
}