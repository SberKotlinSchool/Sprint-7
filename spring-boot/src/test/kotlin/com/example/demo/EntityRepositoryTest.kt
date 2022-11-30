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
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `findByName should find entity` () {

        val savedEntity = entityRepository.save(Entity(name= "R2D2"))
        val foundByNameEntity = entityRepository.findByName(savedEntity.name)
        assertTrue {foundByNameEntity.get() == savedEntity}
    }

    @Test
    fun `findById should find entity` () {
        val savedEntity = entityRepository.save(Entity(name= "Luke"))
        val foundByIdEntity = entityRepository.findById(savedEntity.id!!)
        assertTrue {foundByIdEntity.get() == savedEntity}
    }
}