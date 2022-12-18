package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class DemoJPATests {

    @Autowired
    private  lateinit var  entityRepository: EntityRepository

    @Test
    fun successTest(){
        val savedEntity = entityRepository.save(Entity(value = "1234"))
        val foundEntity = entityRepository.findById(savedEntity.id!!)

        assertEquals(savedEntity, foundEntity.get())
    }
}