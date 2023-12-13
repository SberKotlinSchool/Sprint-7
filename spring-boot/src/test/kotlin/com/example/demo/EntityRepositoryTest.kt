package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest //Чтобы не загружать весь spring-context
class EntityRepositoryTest {
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `jpa test`() {
        val savedEntity = entityRepository.save(Entity(name = "test"))
        val foundEntity = entityRepository.findById(savedEntity.id!!).get()

        assertEquals(savedEntity, foundEntity)
    }
}