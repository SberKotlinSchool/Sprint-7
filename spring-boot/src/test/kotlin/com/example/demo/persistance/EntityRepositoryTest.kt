package com.example.demo.persistance

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
        val saved = repository.save(Entity(name = "Aboba"))

        val found = saved.id?.let { repository.findById(it) }
        assertTrue { found?.get() == saved }
    }
}