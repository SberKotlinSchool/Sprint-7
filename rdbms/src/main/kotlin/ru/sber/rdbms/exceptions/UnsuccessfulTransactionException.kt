package ru.sber.rdbms.exceptions

import java.sql.SQLException

class UnsuccessfulTransactionException: SQLException("Unsuccessfull transaction")
