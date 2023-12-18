package com.example.demo.repository

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {
    @Autowired
    private lateinit var repository: EntityRepository

    @Test
    fun saveAndFindEntity() {
        val entity = Entity(name = "name")
        val savedEntity = repository.save(entity)

        val entityFromDb = savedEntity.id?.let { repository.findById(it) }
        assertTrue { entityFromDb?.get() == savedEntity }
    }
}