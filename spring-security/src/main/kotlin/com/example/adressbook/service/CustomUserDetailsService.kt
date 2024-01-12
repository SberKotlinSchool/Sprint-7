package com.example.adressbook.service

import com.example.adressbook.dto.UserCreateDto
import com.example.adressbook.persistence.entity.AddressUser
import com.example.adressbook.persistence.entity.UserRole
import com.example.adressbook.persistence.repository.AddressUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val addressUserRepository: AddressUserRepository
) : UserDetailsService, UserDetailsPasswordService {

    override fun loadUserByUsername(username: String): UserDetails =
        addressUserRepository.findAddressUserByUserName(username)
            ?.run { User(userName, password, role.authorities) }
            ?: throw UsernameNotFoundException("Пользователь не найден")

    fun createUserIfPossible(user: UserCreateDto) {
        if (!addressUserRepository.existsByUserName(user.username)) {
            with(user) {
                AddressUser(
                    userName = username,
                    password = password,
                    role = role!!
                )
            }.let { addressUserRepository.save(it) }
        }
    }

    override fun updatePassword(user: UserDetails, newPassword: String): UserDetails? {
        addressUserRepository.changePasswordForUser(newPassword, user.username)
        return loadUserByUsername(user.username)
    }
}