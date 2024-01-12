package com.example.adressbook.dto

import com.example.adressbook.persistence.entity.UserRole

data class UserCreateDto(
    val username: String = "",
    var password: String = "",
    var role: UserRole? = null
)
