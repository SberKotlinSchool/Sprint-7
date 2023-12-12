package ru.shadowsith.addressbook.repositories

import org.springframework.data.repository.CrudRepository
import ru.shadowsith.addressbook.dto.User

interface UserRepository: CrudRepository<User, Int> {
    fun findByLogin(login: String): User?
}