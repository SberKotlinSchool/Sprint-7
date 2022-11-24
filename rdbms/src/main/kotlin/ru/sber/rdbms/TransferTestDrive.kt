package ru.sber.rdbms

import java.sql.DriverManager

fun main () {

    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "123"
    )

    //TransferConstraint(connection).transfer(1,2,500)
    //TransferOptimisticLock(connection).transfer(1,2,337)
    TransferPessimisticLock(connection).transfer(1,2,337)

}