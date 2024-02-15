package ru.sber.addressbook.dto

import ru.sber.addressbook.model.ApplicationUser
import ru.sber.addressbook.model.UserRoles

data class LogonDto(
    val username: String,
    val password: String,
    var role: String
) {
  fun toApplicationUser() = ApplicationUser(
      name = username,
      userName = username,
      email = username,
      password = password,
      roles = UserRoles.ROLE_APP
  )
}