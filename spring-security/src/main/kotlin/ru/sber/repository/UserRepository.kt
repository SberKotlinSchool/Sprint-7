package ru.sber.repository

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import ru.sber.model.User
import java.util.concurrent.ConcurrentHashMap

@Repository
class UserRepository {
    private val users = ConcurrentHashMap<String, String>()

    @Value("\${admin.username}")
    private val admin: String = ""

    @Value("\${admin.password}")
    private val password: String = ""

    init {
        addUser(User(admin, password))
    }

    private final fun addUser(user: User) {
        users[user.name] = user.password
    }

    fun findByName(name: String): User? = users[name]?.let { User(name, it) }

    fun deleteByName(name: String) {
        users.remove(name)
    }

}