package com.example.adresbook.dto

import com.example.adresbook.model.ApplicationUser

data class LogonDto(
    val username: String,
    val password: String,
    var role: String
) {
    fun toApplicationUser() = ApplicationUser(
        name = username,
        userName = username,
        email = username,
        password = password,
        roles = emptySet()
    )
}