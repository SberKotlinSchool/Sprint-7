package com.homework.addressbook.repository

import com.homework.addressbook.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun findUserByLogin(login:String) :User?

}