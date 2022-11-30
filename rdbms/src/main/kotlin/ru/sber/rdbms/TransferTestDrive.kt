package ru.sber.rdbms

import java.sql.DriverManager

fun main () {

    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "123"
    )

    //1) Через атомарный инкремент/декримент суммы на счетах(update ... set x=x+100) и ограничение(integrity constraint)
    //TransferConstraint(connection).transfer(1,2,500)

    //2) Через оптимистичные блокировки.
    //TransferOptimisticLock(connection).transfer(1,2,337)

    //3) Через пессимистичные блокировки.
    TransferPessimisticLock(connection).transfer(1,2,337)

}