package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class EntityRepositoryTest {

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `findById should find entity`() {
        val entity = Entity(name = "test")
        val entity2 = Entity(name = "test2")

        val saved = entityRepository.save(entity)
        val saved2 = entityRepository.save(entity2)

        assert(saved.id != saved2.id)

        val found = entityRepository.findById(saved.id)

        assert(found.get().name == "test")
        assert(found.get() == saved)
    }
}