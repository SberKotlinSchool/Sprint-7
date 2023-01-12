package ru.sber.rdbms

fun main() {
    val connectionManager = ConnectionManager()

//    val transferConstraint = TransferConstraint(connectionManager)
//    transferConstraint.transfer(1, 2, 100)

//    val transferOptimisticLock = TransferOptimisticLock(connectionManager)
//    transferOptimisticLock.transfer(2, 1, 100)

    val transferPessimisticLock = TransferPessimisticLock(connectionManager)
    transferPessimisticLock.transfer(2, 1, 100)

    printAccount(1, connectionManager)
    printAccount(2, connectionManager)
}