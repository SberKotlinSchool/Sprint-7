package com.dokl57.ormproject.service

import com.dokl57.ormproject.model.Book
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.dokl57.ormproject.repository.AuthorRepository
import com.dokl57.ormproject.repository.BookRepository

@Service
open class LibraryService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) {
    @Transactional
    open fun addBook(book: Book): Book = bookRepository.save(book)

    @Transactional(readOnly = true)
    open fun findBookById(id: Long): Book? = bookRepository.findById(id).orElse(null)

    @Transactional
    open fun updateBook(book: Book): Book = bookRepository.save(book)

    @Transactional
    open fun deleteBook(id: Long) = bookRepository.deleteById(id)
}
