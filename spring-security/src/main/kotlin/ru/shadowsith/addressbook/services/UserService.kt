package ru.shadowsith.addressbook.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.shadowsith.addressbook.repositories.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return userRepository.findByLogin(username!!) ?: throw UsernameNotFoundException("Пользователь не найден")
    }
}