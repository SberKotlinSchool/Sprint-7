package ru.sber.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.entity.Book
import java.util.*

interface BookRepository : JpaRepository<Book, Long> {
    fun findByTitle(title: String): Optional<Book>
}