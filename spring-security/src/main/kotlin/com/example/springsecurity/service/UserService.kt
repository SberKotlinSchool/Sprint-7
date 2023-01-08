package com.example.springsecurity.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserService {

    fun getUserFromContext(): String {
        val principal = SecurityContextHolder.getContext().authentication.principal

        return if (principal is UserDetails) {
            principal.username
        } else {
            principal as String
        }
    }
}