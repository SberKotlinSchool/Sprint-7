package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.model.UserData
import java.util.*

@Repository
interface UserRepository : CrudRepository<UserData, Long> {
    fun findUserDataByLogin(login: String): Optional<UserData>
}