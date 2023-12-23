package ru.sber.rdbms

data class Account(
    val id: Long,
    val amount: Int,
    val version: Int
)
