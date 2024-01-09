package ru.sber.security.service

import org.springframework.stereotype.Service
import ru.sber.security.dto.User
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthService {
    private var database: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    init {
        database["test"] = "test"
    }

    fun verify(user: User): Boolean {
        return database.get(user.login).equals(user.password) ?: false
    }
}