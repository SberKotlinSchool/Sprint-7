package ru.sber.ufs.cc.kulinich.springmvc.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User
import ru.sber.ufs.cc.kulinich.springmvc.repositories.AuthRepository

@Service
class AuthService @Autowired constructor(val repository: AuthRepository) : UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails {
        val user = repository.findUserByLogin(login)
            .orElseThrow { throw UsernameNotFoundException("User is not found") }
        return User.withUsername(user.login)
            .password(user.password)
            .authorities(*user.roles.map { it.role }.toTypedArray())
            .build()
    }
}