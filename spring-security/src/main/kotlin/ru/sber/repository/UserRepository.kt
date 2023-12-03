package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.model.UserData

@Repository
interface UserRepository : CrudRepository<UserData, Long> {
    fun getUserByLogin(login: String): UserData
}