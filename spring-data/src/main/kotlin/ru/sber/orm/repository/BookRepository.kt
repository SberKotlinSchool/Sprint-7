package ru.sber.orm.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.orm.entities.Book

@Repository
interface BookRepository :JpaRepository <Book, Long>  {

    fun getByTitle(bookName: String): List<Book>?

    fun getByGenreName(genre: String): List<Book>?

    fun getByAuthorName(authorName: String): List<Book>?

}