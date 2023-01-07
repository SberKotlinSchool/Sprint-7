package com.example.notebook.repository

import com.example.notebook.entity.User
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap


@Repository
class UserRep {
    private var userDB = ConcurrentHashMap<String, String>(mapOf("starikovamari" to "12345"))

    fun checkUser(user: User) : Boolean{
        return userDB[user.login] == user.pass
    }

}