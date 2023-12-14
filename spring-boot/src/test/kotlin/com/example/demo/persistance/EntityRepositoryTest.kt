package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun testSaveAndFindById() {
        val entity = Entity(id = 4L, data = "Test Data")

        val savedEntity = entityRepository.save(entity)
        val foundEntity = entityRepository.findById(savedEntity.id).orElse(null)

        assertEquals(entity.id, foundEntity?.id)
    }


    @Test
    fun testDelete() {
        val entity = Entity(id = 1L, data = "Test Data")
        entityRepository.save(entity)

        entityRepository.deleteById(entity.id)

        val foundEntity = entityRepository.findById(entity.id).orElse(null)
        assertEquals(null, foundEntity)
    }

}
