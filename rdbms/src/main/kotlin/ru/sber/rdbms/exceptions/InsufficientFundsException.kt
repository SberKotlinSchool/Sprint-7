package ru.sber.rdbms.exceptions

import java.sql.SQLException

class InsufficientFundsException(accountId: Long): SQLException("Not enough funds on account $accountId")
