package ru.company.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.company.model.User
import java.util.*


@Repository
interface LoginRepository : CrudRepository<User, Long> {
    fun findUserByNickname(login: String): Optional<User>
}