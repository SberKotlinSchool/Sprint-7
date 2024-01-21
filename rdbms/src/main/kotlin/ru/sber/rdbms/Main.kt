package ru.sber.rdbms

fun main() {
//    TransferConstraint().transfer(3, 2, 1500)
//    TransferConstraint().transfer(1, 2, 1500)

//    TransferOptimisticLock().transfer(3, 2, 1500)
//    TransferOptimisticLock().transfer(1, 2, 1500)

    TransferPessimisticLock().transfer(3, 2, 1500)
    TransferPessimisticLock().transfer(2, 3, 99)
    TransferPessimisticLock().transfer(1, 2, 1500)
}
