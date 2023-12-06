package ru.sber.rdbms

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransferOptimisticLockTest : EmbeddedDbTest() {

    @BeforeEach
    fun prepare() {
        clearDb()
    }

    @Test
    fun `transfer from one account to another with transferOptimisticLock`() {
        val id1 = addAccount(10)
        val id2 = addAccount(5)

        db.testDatabase.connection.use { c ->
            val exchange = TransferOptimisticLock(c)
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
            val exchange = TransferOptimisticLock(c)
            exchange.transfer(id1, id2, 50)
        }

        assertAccountAmount(id1, 10)
        assertAccountAmount(id2, 5)

    }
}