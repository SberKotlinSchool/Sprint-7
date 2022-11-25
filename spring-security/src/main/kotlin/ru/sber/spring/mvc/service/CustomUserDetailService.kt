package ru.sber.spring.mvc.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.spring.mvc.repository.UserRepository

@Service
class CustomUserDetailService(val repository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return repository.findByUsername(username!!)?.toUserDetails()?: throw UsernameNotFoundException("ups ...")
    }
}