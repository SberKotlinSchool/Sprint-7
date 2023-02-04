package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.entity.Author
import ru.sber.springjpademo.persistence.entity.Book
import ru.sber.springjpademo.persistence.entity.Library
import ru.sber.springjpademo.persistence.repository.BookRepository

@SpringBootApplication
class SpringJpaDemoApplication(
    private val bookRepository: BookRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val stateLib = Library(name = "State Library")
        val cityLib = Library(name = "City Library")
        val book1 = Book(
            title = "The Lord of the Rings",
            author = Author(firstName = "John", lastName = "Tolkien"),
            libraries = mutableListOf(stateLib, cityLib)
        )

        val martin = Author(firstName = "George", lastName = "Martin")
        val book2 = Book(
            title = "A Song of Ice and Fire",
            author = martin,
            libraries = mutableListOf(stateLib, cityLib)
        )

        val book3 = Book(
            title = "The Winds of Winter",
            author = martin,
            libraries = mutableListOf(stateLib)
        )

        bookRepository.saveAll(listOf(book1, book2, book3))

        var found = bookRepository.findById(book1.id)
        println("Найдена книга: $found \n")

        found = bookRepository.findByTitle(book2.title)
        println("Найдена книга: $found \n")

        book1.title = "${book1.title} 2"
        var updated = bookRepository.save(book1)
        println("Обновлена книга: $updated \n")

        bookRepository.delete(book2)

        val allBooks = bookRepository.findAll()
        println("Все книги: $allBooks")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
