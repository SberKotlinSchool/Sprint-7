package ru.sber.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

}