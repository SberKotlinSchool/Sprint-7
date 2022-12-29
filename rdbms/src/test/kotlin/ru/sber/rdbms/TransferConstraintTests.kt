package ru.sber.rdbms

import org.junit.jupiter.api.Test

class TransferConstraintTests {

    private var transferConstraint = TransferConstraint()

    @Test
    fun `should successful execute query`() {
        transferConstraint.transfer(4, 2, 2)
    }
}