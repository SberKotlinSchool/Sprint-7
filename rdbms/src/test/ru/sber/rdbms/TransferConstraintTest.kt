package ru.sber.rdbms

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.postgresql.util.PSQLException

internal class TransferConstraintTest {

    lateinit var transferConstraint: TransferConstraint

    @BeforeEach
    fun setup() {
        TransferTestUtils.initAccounts(1000, 1500)
        transferConstraint = TransferConstraint()
    }

    @Test
    fun transferNormalTest() {
        assertDoesNotThrow { transferConstraint.transfer(1, 2, 500) }
    }

    @Test
    fun transferOverdraftTest() {
        assertThrows(PSQLException::class.java) { transferConstraint.transfer(1, 2, 1001) }
    }

    @Test
    fun transferReversedNormalTest() {
        assertDoesNotThrow { transferConstraint.transfer(2, 1, 1001) }
    }

    @Test
    fun transferReversedOverdraftTest() {
        assertThrows(PSQLException::class.java) { transferConstraint.transfer(2, 1, 1501) }
    }
}