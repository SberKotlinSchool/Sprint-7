package ru.sber.rdbms

data class AccountEntity(
        val id: Long,
        val amount: Int,
        val version: Int
)