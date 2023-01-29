package sber.mvc.application.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import sber.mvc.application.repository.UserRepository

@Service
class UserService @Autowired constructor(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(login: String): UserDetails {
        val user = userRepository.findUserByLogin(login)
            .orElseThrow { throw UsernameNotFoundException("User is not found") }
        return User.withUsername(user.login)
            .password(user.password)
            .authorities(*user.roles.map { it.role }.toTypedArray())
            .build()
    }
}