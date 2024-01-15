package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.entity.Author
import ru.sber.springjpademo.persistence.entity.Book
import ru.sber.springjpademo.persistence.entity.Language
import ru.sber.springjpademo.persistence.repository.BookRepository

@SpringBootApplication
class SpringDataApplication(private val bookRepository: BookRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val firstBook =
            Book(
                title = "First Film",
                localTitle = "First Film Local",
                authors = mutableSetOf(Author(name = "First Author"), Author(name = "Second Author")),
                language = Language(name = "First Language")
            )
        val secondBook =
            Book(
                title = "Second Book",
                localTitle = "Second Book Local",
                authors = mutableSetOf(Author(name = "Third Author")),
                language = Language(name = "Second Language")
            )
        val thirdBook =
            Book(
                title = "Third Book",
                localTitle = "Third Book Local",
                authors = mutableSetOf(
                    Author(name = "First Author"),
                    Author(name = "Second Author"),
                    Author(name = "Third Author")
                ),
                language = Language(name = "Third Language")
            )

        bookRepository.save(firstBook)
        bookRepository.save(secondBook)
        bookRepository.save(thirdBook)

        var found = bookRepository.findById(firstBook.book_id)
        println("book found: $found \n")

        val allBooks = bookRepository.findAll()
        println("all books: $allBooks")

        bookRepository.delete(firstBook)

        secondBook.language = Language(name = "Third Language")

        bookRepository.save(secondBook)

    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}