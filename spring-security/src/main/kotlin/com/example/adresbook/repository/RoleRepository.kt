package com.example.adresbook.repository

import com.example.adresbook.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<UserRole, Long> {
    fun findAllByName(name: String): Set<UserRole>
}