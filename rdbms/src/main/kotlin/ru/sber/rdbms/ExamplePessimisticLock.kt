package ru.sber.rdbms

/**
create table account1
(
id bigserial constraint account_pk primary key,
amount int
);
 */
fun main() {
    val transferPessimisticLock = TransferPessimisticLock()

    transferPessimisticLock.transfer(1, 2, 300)
}


