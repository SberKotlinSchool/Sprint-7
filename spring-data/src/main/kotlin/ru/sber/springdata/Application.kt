package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.entity.Author
import ru.sber.springdata.entity.Book
import ru.sber.springdata.entity.Library
import ru.sber.springdata.repository.BookRepository

@SpringBootApplication
class Application(private val bookRepository: BookRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val schoolLib = Library(name = "School Library")
        val univLib = Library(name = "University Library")
        val book1 = Book(
            title = "One",
            author = Author(firstName = "Bob", lastName = "Lee"),
            libraries = mutableListOf(schoolLib, univLib)
        )

        val ben = Author(firstName = "Ben", lastName = "Po")
        val book2 = Book(
            title = "Two",
            author = ben,
            libraries = mutableListOf(schoolLib, univLib)
        )

        val book3 = Book(
            title = "Three",
            author = ben,
            libraries = mutableListOf(schoolLib)
        )

        bookRepository.saveAll(listOf(book1, book2, book3))

        var found = bookRepository.findById(book1.id)
        println("One: $found \n")

        found = bookRepository.findByTitle(book2.title)
        println("Two: $found \n")

        book1.title = "${book1.title} 2"
        var updated = bookRepository.save(book1)
        println("Three: $updated \n")

        bookRepository.delete(book2)

        val allBooks = bookRepository.findAll()
        println("All: $allBooks")
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}