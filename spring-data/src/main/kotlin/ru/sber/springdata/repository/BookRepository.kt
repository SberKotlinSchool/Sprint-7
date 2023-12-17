package ru.sber.springdata.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.springdata.entity.Book
import java.util.*

interface BookRepository : JpaRepository<Book, Long> {
    fun findByTitle(title: String): Optional<Book>
}