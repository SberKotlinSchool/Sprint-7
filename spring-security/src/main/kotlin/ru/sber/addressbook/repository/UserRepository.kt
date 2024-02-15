package ru.sber.addressbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.addressbook.model.ApplicationUser

@Repository
interface UserRepository: JpaRepository<ApplicationUser, Long> {
  fun findByUserName(userName: String): ApplicationUser?
}