package com.example.mvcexampleproject.repository

import com.example.mvcexampleproject.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component


@Component
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}