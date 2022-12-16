package com.sbuniver.homework.repository

import com.sbuniver.homework.entity.User
import org.springframework.data.repository.CrudRepository

interface UserReposirory : CrudRepository<User, Long> {
    fun findByUsername(username: String?): User
}