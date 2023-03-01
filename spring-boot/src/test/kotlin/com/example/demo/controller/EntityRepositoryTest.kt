package com.example.demo.controller

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `get Entities by name`() {
        // Give
        val name = "John"
        val savedEntity = entityRepository.save(Entity(name = name))
        // When
        val res = entityRepository.getByName(name)
        // Then
        assertEquals(savedEntity, res.first())
    }
}