package ru.sber.addressbook.service

import org.springframework.stereotype.Service
import ru.sber.addressbook.dto.LoginDto
import ru.sber.addressbook.dto.LogonDto
import ru.sber.addressbook.model.ApplicationUser
import ru.sber.addressbook.model.UserRoles
import ru.sber.addressbook.repository.UserRepository

@Service
class UserService(
    private val customUserDetailsService: CustomUserDetailsService,
    private val userRepository: UserRepository,
) {
  fun loginUser(loginDto: LoginDto) {
      customUserDetailsService.loadUserByUsername(loginDto.username)
  }

  fun logonUser(logonDto: LogonDto): ApplicationUser {

    if (userRepository.findByUserName(logonDto.username) != null) {
      throw Exception("Пользователь существует")
    }

    val appUser: ApplicationUser = logonDto.toApplicationUser()

    appUser.roles = UserRoles.ROLE_APP

    return userRepository.save(appUser)
  }

}