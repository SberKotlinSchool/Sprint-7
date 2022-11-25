package ru.sber.spring.mvc.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.spring.mvc.model.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}