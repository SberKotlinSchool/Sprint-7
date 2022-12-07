package ru.sber.AddressBook.Services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.AddressBook.Repositories.UserRepository

@Service
class CustomUserDetailService @Autowired constructor(val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUserName(username)?.toUserDetails() ?: throw UsernameNotFoundException("User with username $username not found")
}