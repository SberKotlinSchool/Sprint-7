package com.example.demo

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
    fun findById() {
        val entity = repository.save(Entity(name = "Anton"))

        val result = repository.findById(entity.id!!)
        assertTrue { result.get() == entity }
    }
}