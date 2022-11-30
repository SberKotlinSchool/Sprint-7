package ru.sber.spring.mvc.auth

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.spring.mvc.model.UserCredential

interface UserCredentialRepository : JpaRepository<UserCredential, Long> {
    fun findByUsername(username: String): UserCredential?
}