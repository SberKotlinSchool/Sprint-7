package ru.sber.addressbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.addressbook.model.UserRole

@Repository
interface RoleRepository: JpaRepository<UserRole, Long> {
  fun findAllByName(name: String): Set<UserRole>
}