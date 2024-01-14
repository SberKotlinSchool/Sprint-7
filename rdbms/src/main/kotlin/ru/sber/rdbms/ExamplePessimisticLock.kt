package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

/**
create table account1
(
id bigserial constraint account_pk primary key,
amount int
);
 */
fun main() {
  val transferPessimistic = TransferPessimisticLock()

  transferPessimistic.transfer(1,2, 5)
}


