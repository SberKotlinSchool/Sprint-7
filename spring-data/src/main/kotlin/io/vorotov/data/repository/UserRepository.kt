package io.vorotov.data.repository

import io.vorotov.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository: JpaRepository<User, Long> {

    @Query("SELECT u FROM User u where u.portfolio is null")
    fun findAllWithNoPortfolio(): List<User>

}