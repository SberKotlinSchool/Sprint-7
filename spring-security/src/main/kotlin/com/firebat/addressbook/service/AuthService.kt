package com.firebat.addressbook.service

import com.firebat.addressbook.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthService(val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(p0: String?): UserDetails {
        return userRepository.findByUsername(p0!!)?.toUserDetails()
            ?: throw UsernameNotFoundException("User named $p0 not found")
    }
}