package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class DataJpaTests {
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `save entity to db`() {
        val savedEntity = entityRepository.save(
            Entity(
                name = "name"
            )
        )
        assertTrue(entityRepository.existsById(savedEntity.id!!))
        assertTrue(entityRepository.getById(savedEntity.id!!).name == "name")
    }

    @Test
    fun `delete entity in db`() {
        val savedEntity = entityRepository.save(
            Entity(
                name = "for delete"
            )
        )
        entityRepository.deleteById(savedEntity.id!!)

        assertFalse(entityRepository.existsById(2))
    }

    @Test
    fun `update entity in db`() {
        val savedEntity = entityRepository.save(
            Entity(
                name = "for update"
            )
        )
        entityRepository.getById(savedEntity.id!!).name = "Updated!"

        assertTrue(entityRepository.getById(savedEntity.id!!).name == "Updated!")
    }

    @Test
    fun `save few entities to db`() {
        entityRepository.saveAll(
            generateEntities(20)
        )

        assertTrue(entityRepository.count() == 20L)
    }

    @Test
    fun `delete few entities from db`() {
        entityRepository.saveAll(
            generateEntities(20)
        )

        entityRepository.deleteAll()

        assertTrue(entityRepository.count() == 0L)
    }


    private fun generateEntities(amount: Int): List<Entity> {
        val result= mutableListOf<Entity>()
        for (i in 1..amount) {
            result.add(
                Entity(
                    name = "name$i"
                )
            )
        }
        return result
    }
}