package ru.morningcake.addressbook.constant

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ADMIN, USER, DELETER;

    override fun getAuthority(): String? {
        return name
    }
}