package ru.sber.addressbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.addressbook.model.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByLogin(login: String): User
}