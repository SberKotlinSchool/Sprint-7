package ru.sber.springjpademo.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.springjpademo.persistence.entity.Book
import java.util.Optional

interface BookRepository : JpaRepository<Book, Long> {
    fun findByTitle(title: String): Optional<Book>
}
