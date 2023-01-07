package ru.sber.rdbms

import org.junit.jupiter.api.Test

internal class TransferConstraintTest {

    @Test
    fun transferSuccess() {
        val transferConstraint = TransferConstraint()

        val result = transferConstraint.transfer(1, 0, 50)
        assert(result)
    }

    @Test
    fun transferFail() {
        val transferConstraint = TransferConstraint()

        val result = transferConstraint.transfer(1, 0, 5000)
        assert(!result)
    }
}