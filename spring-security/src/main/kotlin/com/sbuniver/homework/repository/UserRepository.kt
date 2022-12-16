package com.sbuniver.homework.repository

import com.sbuniver.homework.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String?): Optional<User>
}