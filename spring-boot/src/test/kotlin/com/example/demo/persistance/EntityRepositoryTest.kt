package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest{
    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun getByNameTest() {
        val name = "Ivan"
        val savedEntity = entityRepository.save(Entity(name = name))
        val res = entityRepository.getByName(name)
        assertEquals(savedEntity, res.first())
    }
}