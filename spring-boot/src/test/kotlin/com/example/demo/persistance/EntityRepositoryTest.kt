package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `getById should return entity`() {
        val savedEntity = entityRepository.save(Entity(name = "test"))
        val foundEntity = entityRepository.findById(savedEntity.id!!)
        assertTrue { foundEntity.get() == savedEntity }
    }
}