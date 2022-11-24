package ru.sber.rdbms

import java.sql.DriverManager

fun main () {

    val connection = DriverManager.getConnection(
        "jdbc:postgresql://10.56.83.49:5433/tkp_dev6",
        "123",
        "123"
    )

    //TransferConstraint(connection).transfer(1,2,500)
    //TransferOptimisticLock(connection).transfer(1,2,337)
    TransferPessimisticLock(connection).transfer(1,2,337)

}