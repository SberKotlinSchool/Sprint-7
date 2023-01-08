package com.example.springsecurity.service

import com.example.springsecurity.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(login: String): UserDetails =
        userRepository.findUserByLogin(login)?.toUserDetails() ?: throw UsernameNotFoundException("User $login not found")

}