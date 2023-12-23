package com.example.demo.service

import com.example.demo.model.Book
import com.example.demo.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun save(book: Book) = bookRepository.save(book)

    fun get(id: Long) = bookRepository.findById(id)

    fun update(book: Book) = bookRepository.save(book)

    fun delete(book: Book) = bookRepository.delete(book)
}