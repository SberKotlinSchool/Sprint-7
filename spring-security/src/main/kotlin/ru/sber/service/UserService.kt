package ru.sber.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.repository.UserRepository


@Service
class UsersService(@Autowired val repository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails {
        log.info { login }
        val user = repository.findUserDataByLogin(login)
            .orElseThrow { throw UsernameNotFoundException("User is not found") }
        log.info { user }
        return User.withUsername(user.login)
            .password(user.password)
            .authorities(*user.roles.map { it.roles }.toTypedArray())
            .build()
    }

    companion object {
        val log = KotlinLogging.logger {}
    }
}