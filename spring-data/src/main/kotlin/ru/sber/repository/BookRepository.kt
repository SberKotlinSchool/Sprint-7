package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.enteties.Book
import ru.sber.enteties.BookCategory

@Repository
interface BookRepository : CrudRepository<Book, Long> {

    fun findByCategory(category: BookCategory): List<Book>
}