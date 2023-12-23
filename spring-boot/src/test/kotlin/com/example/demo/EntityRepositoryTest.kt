package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.junit.jupiter.api.Assertions.assertEquals

@DataJpaTest
class EntityRepositoryTest {

    @Autowired private lateinit var entityRepository: EntityRepository

    @Test
    fun `Test EntityRepository`() {

        val insertRow = entityRepository.save(Entity(name = "Name1"))

        println(insertRow.id)
        println(insertRow.name)

        assertEquals(insertRow, entityRepository.getById(insertRow.id!!))
    }
}