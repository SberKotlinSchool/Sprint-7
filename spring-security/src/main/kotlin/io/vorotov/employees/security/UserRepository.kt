package io.vorotov.employees.security

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserData, Long> {
    fun findByUsername(username: String): UserData?
}