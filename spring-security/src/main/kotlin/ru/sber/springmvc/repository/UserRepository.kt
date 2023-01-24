package ru.sber.springmvc.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.springmvc.model.User
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    fun findByLogin(username: String): Optional<User>

}