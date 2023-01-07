package ru.sber.springsecurity.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.springsecurity.repositories.UserRepository

@Service
class UserService @Autowired constructor(val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findUserByName(username)?.toUserDetails() ?: throw UsernameNotFoundException("User with username $username not found")
    }
}