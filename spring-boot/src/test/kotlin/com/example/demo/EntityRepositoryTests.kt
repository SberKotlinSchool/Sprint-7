package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTests {

    private companion object {

        val TEST_ENTITY = Entity(1, "test description")
    }

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `test repository interaction`() {
        val stored = entityRepository.save(TEST_ENTITY)

        assertEquals(TEST_ENTITY, stored)
        assertEquals(TEST_ENTITY, entityRepository.findById(stored.id!!).get())
    }
}
