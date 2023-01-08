package com.example

import com.example.persistance.Entity
import com.example.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {

    @Autowired
    private  lateinit var  entityRepository: EntityRepository

    @Test
    fun findById(){
        val savedEntity = entityRepository.save(Entity(name = "Anonym"))
        val foundEntity = entityRepository.findById(savedEntity.id).get()

        assertEquals(savedEntity, foundEntity)
    }
}