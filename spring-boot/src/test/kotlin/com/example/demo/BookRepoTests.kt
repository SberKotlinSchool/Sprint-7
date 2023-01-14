package com.example.demo

import com.example.demo.persistance.BookEntity
import com.example.demo.persistance.BookRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class BookRepoTests {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun shouldFindBookById() {
        val savedEntity = bookRepository.save(BookEntity(1, "Anna Karenina", "Tolstoy"))

        val foundEntity = bookRepository.findById(1)
        Assertions.assertTrue { foundEntity.get() == savedEntity }
    }
}
