package ru.sber.rdbms

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        TODO()
    }
}

fun main() {
    val tc = TransferPessimisticLock()
    tc.transfer(1, 2, 100)
}
