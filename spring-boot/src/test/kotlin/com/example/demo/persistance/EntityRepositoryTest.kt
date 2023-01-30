package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
internal class EntityRepositoryTest {

    @Autowired
    private lateinit var entityRepository: EntityRepository


    @Test
    fun `findById should find entity`() {
        val testEntity = getRandomEntity()

        val savedEntity = saveEntity(testEntity)

        val foundEntity = entityRepository.findById(savedEntity.id!!)

        assertTrue { foundEntity.get() == savedEntity }
    }

    @Test
    fun `delete should delete entity`() {
        val testEntity = getRandomEntity()

        val savedEntity = saveEntity(testEntity)

        entityRepository.deleteById(savedEntity.id!!)

        assertTrue { entityRepository.findById(savedEntity.id!!).isEmpty }
    }


    private fun saveEntity(entity: Entity): Entity = entityRepository.save(entity)
        .also {
            assertNotNull { it.id }
        }

    private fun getRandomEntity() = Entity(name = UUID.randomUUID().toString())
}
