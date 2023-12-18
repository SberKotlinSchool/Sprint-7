package com.dokl57.ormproject

import com.dokl57.ormproject.config.AppConfiguration
import com.dokl57.ormproject.model.Author
import com.dokl57.ormproject.model.Book
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import com.dokl57.ormproject.service.LibraryService

fun main() {
    val context = AnnotationConfigApplicationContext(AppConfiguration::class.java)
    val libraryService = context.getBean(LibraryService::class.java)

    val author = Author(name = "Лев Толстой")
    val book = Book(title = "Война и мир", author = author)

    val savedBook = libraryService.addBook(book)
    println("Книга с названием ${savedBook.title} сохранена. Идентификатор ${savedBook.id}")


    val readBook = libraryService.findBookById(savedBook.id)
    println("Книга с идентификатормо ${savedBook.id} имеет название ${readBook?.title}")

    if (readBook != null) {
        readBook.title = "Анна Каренина"
        libraryService.updateBook(readBook)
        println("Книга обновлена: ${readBook.title}")
    }

    readBook?.let { libraryService.deleteBook(it.id) }
    println("Книга удалена")
}

