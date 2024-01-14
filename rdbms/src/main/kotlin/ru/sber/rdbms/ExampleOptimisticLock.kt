package ru.sber.rdbms

/**
create table account1
(
id bigserial constraint account_pk primary key,
amount int,
version int
);
 */
fun main() {
    val transferOptimisticLock = TransferOptimisticLock()

    transferOptimisticLock.transfer(1, 2, 300)
}


