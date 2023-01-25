package ru.sber.springmvc.service

import ru.sber.springmvc.model.FindUserDTO
import ru.sber.springmvc.model.User
import java.util.*

interface UserService {
    fun getUserList() : List<User>

    fun findById(id: Long): Optional<User>
    fun findByLogin(login: String) : Optional<User>
    fun saveUser(user: User) : User
    fun findUser(findUserDTO: FindUserDTO): List<User>
    fun deleteUser(id : Long)
}