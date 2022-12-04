package com.example.demo.persistance

import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

}