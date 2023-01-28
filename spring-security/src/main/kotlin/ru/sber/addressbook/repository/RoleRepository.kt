package ru.sber.addressbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.addressbook.model.Role

@Repository
interface RoleRepository : JpaRepository<Role, Long>