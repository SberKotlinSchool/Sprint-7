package ru.sber.kotlinmvc.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = Optional.ofNullable(AppUser.values().firstOrNull { it.login == username })
            .orElseThrow { UsernameNotFoundException("пользователь ${username} не найден") }

        return User.withUsername(user.login)
            .password(user.password)
            .authorities(*user.roles.toTypedArray())
            .build()
    }

}

enum class AppUser (val login: String, val password: String, val roles: List<AppRole>) {
    ADMIN("admin", "admin", listOf(AppRole.ADMIN)),
    USER("user", "user", listOf(AppRole.USER)),
    SUPERUSER("superuser", "superuser", listOf(AppRole.USER, AppRole.API))
}

enum class AppRole : GrantedAuthority {
    ADMIN,
    USER,
    API;

    override fun getAuthority(): String {
        return this.name
    }
}