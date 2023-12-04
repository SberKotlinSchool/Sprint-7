package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class StudentRepositoryTest {
    @Autowired
    private lateinit var studentRepository: StudentRepository

    @Test
    fun `findById should find student`() {
        // given
        val savedEntity = studentRepository.save(Student(name = "name", group = "1prk"))

        // when
        val foundEntity = studentRepository.findById(savedEntity.id!!)

        // then
        assertTrue { foundEntity.get() == savedEntity }
    }
}