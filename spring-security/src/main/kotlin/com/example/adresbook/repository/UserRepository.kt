package com.example.adresbook.repository

import com.example.adresbook.model.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<ApplicationUser, Long>{
    fun findByUserName(userName: String): ApplicationUser?
}