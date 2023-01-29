package ru.sber.astafex.springmvc.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.astafex.springmvc.repository.UserRepository

@Service
class CustomUserDetailService(private val repository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
        repository.findByUsername(username!!)?.toUserDetails() ?: throw UsernameNotFoundException("User not found")
}