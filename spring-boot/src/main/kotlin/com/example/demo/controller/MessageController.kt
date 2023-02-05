package com.example.demo.controller

import com.example.demo.persistance.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MessageController(private val messageRepository: MessageRepository) {

    @GetMapping("/message/{id}")
    fun helloWorld(@PathVariable id: Long): String {
        return messageRepository.findOneById(id)?.message ?: "Сообщение с указанным Id не найдено"
    }

}