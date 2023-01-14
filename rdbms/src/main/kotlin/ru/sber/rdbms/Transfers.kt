package ru.sber.rdbms

import ru.sber.rdbms.constraints.TransferConstraint
import ru.sber.rdbms.constraints.TransferOptimisticLock
import ru.sber.rdbms.constraints.TransferPessimisticLock

internal const val DB_URL = "jdbc:postgresql://localhost:5432/db"
internal const val DB_USER = "postgres"
internal const val DB_PASS = "postgres"

fun main() {
    TransferConstraint().transfer(1, 2, 100)

    TransferOptimisticLock().transfer(1, 2, 100)

    TransferPessimisticLock().transfer(1, 2, 100)
}
