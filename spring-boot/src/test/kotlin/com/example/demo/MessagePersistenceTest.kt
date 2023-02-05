package com.example.demo

import com.example.demo.persistance.Message
import com.example.demo.persistance.MessageRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MessagePersistenceTest {

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Test
    @Disabled
    fun `save and find test`() {
        val message = Message().apply { message = "my message" }
        messageRepository.save(message).let {
            Assertions.assertEquals(message, messageRepository.findOneById(it.id))
        }
    }

}