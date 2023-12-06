package ru.sber.rdbms

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransferConstraintTest : EmbeddedDbTest() {

    @BeforeEach
    fun prepare() {
        clearDb()
    }

    @Test
    fun `transfer from one account to another with transferConstraint`() {
        val id1 = addAccount(10)
        val id2 = addAccount(5)

        db.testDatabase.connection.use { c ->
            val exchange = TransferConstraint(c)
            exchange.transfer(id1, id2, 3)
        }

        assertAccountAmount(id1, 7)
        assertAccountAmount(id2, 8)

    }

    @Test
    fun `transfer more money than it have`(){
        val id1 = addAccount(10)
        val id2 = addAccount(5)

        db.testDatabase.connection.use { c ->
            val exchange = TransferConstraint(c)
            exchange.transfer(id1, id2, 50)
        }

        assertAccountAmount(id1, 10)
        assertAccountAmount(id2, 5)

    }

}