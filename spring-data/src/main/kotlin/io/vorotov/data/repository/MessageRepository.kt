package io.vorotov.data.repository

import io.vorotov.data.entity.Message
import io.vorotov.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository: JpaRepository<Message, Long> {

    fun findAllByUser(user: User): List<Message>

}