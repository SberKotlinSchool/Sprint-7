package ru.sber.rdbms

import java.sql.DriverManager

fun main() {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "test_user",
        "test_user"
    )

//    TransferConstraint(connection).transfer(1, 2, 300)
 //   TransferConstraint(connection).transfer(1, 2, 50)
//
//    TransferOptimisticLock(connection).transfer(1, 2, 300)
//    TransferOptimisticLock(connection).transfer(1, 2, 100)
//
    TransferPessimisticLock(connection).transfer(1, 2, 300)
//    TransferPessimisticLock(connection).transfer(1, 2, 150)
}

