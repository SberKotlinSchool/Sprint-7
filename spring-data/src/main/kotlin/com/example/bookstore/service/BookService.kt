package com.example.bookstore.service

import com.example.bookstore.entity.Book
import com.example.bookstore.repository.BookRepository
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookService @Autowired constructor(val bookRepository: BookRepository)
{
    fun fetchAll(): List<Book> {
        return bookRepository.findAll()
    }

    fun findById(id: Long): Book? {
       return bookRepository.findById(id).orElse(null)
    }

    fun findByName(name: String): Book? {
       return bookRepository.findByName(name)
    }

    fun findByAuthorId(authorId: Long): Book? {
        return bookRepository.findByAuthorId(authorId)
    }

    fun save(book: Book) {
        bookRepository.save(book)
    }

    fun saveAll(books: List<Book>) {
        bookRepository.saveAll(books)
    }
}