package com.example.demo

import com.example.demo.model.Entity
import com.example.demo.model.EntityRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class RepositoryTest {

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `findById should save and then find entity`() {
        val savedEntity = entityRepository.save(Entity(ammount = 100))
        val foundEntity = entityRepository.findById(savedEntity.id!!)
        assertTrue { foundEntity.get() == savedEntity }
    }
}