package ru.sber.rdbms.exception

import java.sql.SQLException

class ImpossibleOperationException (message: String): SQLException(message)