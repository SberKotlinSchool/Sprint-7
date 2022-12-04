package com.example.demo.persistance

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AuthorRepositoryTest {

    @Autowired
    private lateinit var repository: AuthorRepository

    val author = Author(1, firstName = "Test", secondName = "Testov")

    @BeforeAll
    fun setUp() {
        repository.save(author)
    }

    @Test
    fun `findById should find entity`() {
        val foundUser = repository.findById(author.id!!)
        assertTrue { foundUser.get() == author }
    }

    @Test
    fun `findBySecondName should find entity`() {
        val foundUsers = repository.findAllBySecondName(author.secondName!!)
        assertNotNull(foundUsers)
        assertEquals(1, foundUsers?.size)
        assertTrue { foundUsers?.get(0) == author }
    }
}