package ru.sber.mvc.models

// по заданию вроде необязательно толкать это все в БД?
// если сильно надо, могу затолкать...

data class Address(var id: Int? = null, var name: String = "", var phone: String = "", var descr: String = "")
