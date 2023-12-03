package ru.sber.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.sber.model.UserData
import ru.sber.repository.UserRepository

@Service
class UserService(@Autowired private val userRepository: UserRepository) {

    fun isUserCorrect(userData: UserData): Boolean {
        val userInRepository = userRepository.getUserByLogin(userData.login)
        if (userData.password == userInRepository.password) {
            return true
        }
        return false
    }
}