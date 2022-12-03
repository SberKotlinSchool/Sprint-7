package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.model.User
import java.util.*


@Repository
interface LoginRepository : CrudRepository<User, Long> {
    fun findUserByLogin(login: String): Optional<User>
}