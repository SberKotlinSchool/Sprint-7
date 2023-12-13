package ru.sber.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.persistence.UserAuthorityRepository

@Service
class UserDetailSecurityService(
        val userAuthorityRepository: UserAuthorityRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userAuthorityRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found")
        return CustomUserDetails(user)
    }

}