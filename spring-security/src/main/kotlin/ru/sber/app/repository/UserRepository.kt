package ru.sber.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.app.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

}