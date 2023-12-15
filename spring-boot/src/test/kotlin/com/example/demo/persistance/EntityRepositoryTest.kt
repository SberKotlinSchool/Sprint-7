package com.example.demo.persistance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun findByIdSuccessTest() {
        val savedEntity = entityRepository.save(Entity(name = "Test"))
        val foundEntity = entityRepository.findById(1).orElse(null)
        Assertions.assertEquals(savedEntity, foundEntity)
    }
}