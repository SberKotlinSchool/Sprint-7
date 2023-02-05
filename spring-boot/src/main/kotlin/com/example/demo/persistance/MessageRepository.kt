package com.example.demo.persistance

import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {

    fun findOneById(id: Long?): Message?

}