package ru.sber.springsecurity.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.springsecurity.entities.User

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findUserByName(name: String): User?
}