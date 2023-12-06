package ru.sber.rdbms

import java.sql.SQLException

class NotEnoughMoneyException(): SQLException("Not enough money") {

}