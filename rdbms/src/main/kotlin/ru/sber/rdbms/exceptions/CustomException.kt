package ru.sber.rdbms.exception

import java.sql.SQLException

class CustomException(message: String?) : SQLException(message)