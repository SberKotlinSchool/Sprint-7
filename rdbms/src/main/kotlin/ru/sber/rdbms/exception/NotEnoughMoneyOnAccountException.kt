package ru.sber.rdbms.exception

import java.sql.SQLException

class NotEnoughMoneyOnAccountException(message: String?) : SQLException(message)