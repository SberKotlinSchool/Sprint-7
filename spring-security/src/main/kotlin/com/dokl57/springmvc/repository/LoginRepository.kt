package com.dokl57.springmvc.repository


import com.dokl57.springmvc.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface LoginRepository : CrudRepository<User, Long> {
    fun findUserByLogin(login: String): Optional<User>
}