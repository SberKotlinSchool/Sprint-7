package com.example.adresbook.service

import com.example.adresbook.dto.LogonDto
import com.example.adresbook.model.ApplicationUser
import com.example.adresbook.model.UserRole
import com.example.adresbook.repository.RoleRepository
import com.example.adresbook.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUserName(username)
            .let {
                if (it != null)
                    User(it.userName, it.password, it.roles.map { role -> SimpleGrantedAuthority(role.name) })
                else throw UsernameNotFoundException("User not found")
            }
}