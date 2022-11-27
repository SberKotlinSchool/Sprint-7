package com.example.demo.persistence

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class EntityRepositoryTest {
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `findById test`() {
        // given
        val savedEntity = entityRepository.save(Entity(name = "Example"))

        // when
        val foundEntity = entityRepository.findById(savedEntity.id!!).get()

        // then
        assertEquals(foundEntity, savedEntity)
        assertEquals("Example", foundEntity.name)
    }
}