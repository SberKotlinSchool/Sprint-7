package io.vorotov.employees.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import io.vorotov.employees.security.UserRepository

@Service
class CustomUserDetailService(
    private val repository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userData = repository.findByUsername(username) ?: throw UsernameNotFoundException("Пользователь не найден")
        return userData.toUserDetails()
    }
}