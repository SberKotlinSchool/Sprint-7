package ru.sber.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.repository.LoginRepository


@Service
class LoginService @Autowired constructor(val repository: LoginRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findUserByLogin(username)
            .orElseThrow { throw UsernameNotFoundException("User is not found") }
        return User.withUsername(user.login)
            .password(user.password)
            .authorities(*user.roles.map { it.role }.toTypedArray())
            .build()
    }
}