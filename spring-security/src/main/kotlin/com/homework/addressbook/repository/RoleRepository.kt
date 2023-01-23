package com.homework.addressbook.repository

import com.homework.addressbook.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {

}