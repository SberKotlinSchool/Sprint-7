package com.example.mvcexampleproject.services

import com.example.mvcexampleproject.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(private val repository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return repository.findByUsername(username!!)?.toUserDetails() ?: throw UsernameNotFoundException("Пользователь не найден")
    }
}