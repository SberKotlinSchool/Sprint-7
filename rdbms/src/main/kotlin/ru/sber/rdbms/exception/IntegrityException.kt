package ru.sber.rdbms.exception

import java.sql.SQLException

class IntegrityException :
    SQLException("Данные, изменяемые в ходе данной транзакции, были изменены другой транзакцией!") {
}