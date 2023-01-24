package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private lateinit var rep: ClientRepository

    @Test
    fun findByIdSuccessTest() {
        // given
        val savedClient = rep.save(Client(lastName = "Попов", firstName = "Иван", middleName = "Сергеевич",
            phoneNumber = "+7 999 999-99-99", email = "popovi@mail.ru"))
        // when
         val foundEntity = rep.findById(savedClient.id!!)
        // then
        assertTrue { foundEntity.get() == savedClient }
    }

}