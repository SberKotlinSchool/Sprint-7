package ru.sber.security.dto

data class Entity (
    val entityId: Long? = null,
    val fullName: String? = null,
    val fullAddress: String? = null,
    val phoneNumber: String? = null
)