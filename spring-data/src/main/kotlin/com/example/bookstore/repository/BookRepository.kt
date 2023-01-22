package com.example.bookstore.repository

import com.example.bookstore.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {

    fun findByName (name: String): Book?

    fun findByAuthorId (authorId: Long): Book?

}