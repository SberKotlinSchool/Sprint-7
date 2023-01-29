package ru.sber.astafex.springmvc.service

import org.springframework.stereotype.Service
import ru.sber.astafex.springmvc.model.User
import ru.sber.astafex.springmvc.repository.UserRepository

@Service
class UserService(private val repository: UserRepository) {
    fun check(user: User): Boolean = repository.get(user.login)?.equals(user.password) ?: false
}