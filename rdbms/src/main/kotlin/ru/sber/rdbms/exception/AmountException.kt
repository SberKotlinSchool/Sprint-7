package ru.sber.rdbms.exception

import java.sql.SQLException

class AmountException : SQLException("Сумма на счете не может уходить в минус!") {
}