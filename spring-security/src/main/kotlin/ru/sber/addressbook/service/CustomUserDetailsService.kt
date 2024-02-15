package ru.sber.addressbook.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.addressbook.repository.UserRepository

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails =
      userRepository.findByUserName(username)
          ?.run { User(userName, password, roles.authorities) }
          ?: throw UsernameNotFoundException("Пользователь не найден")
}