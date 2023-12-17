package ru.sber

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.enteties.Author
import ru.sber.enteties.Book
import ru.sber.enteties.BookCategory
import ru.sber.enteties.Publish
import ru.sber.repository.BookRepository

@SpringBootApplication
class SpringDataApplication(

    private val bookRepository: BookRepository,
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        var book1 = Book(
            name = "Сказки",
            category = BookCategory.Story,
            publish = Publish(name = "publish"),
            authot = mutableListOf(
                Author(firstName = "Александр", lastName = "Пушкин"),
                Author(firstName = "Корней", lastName = "Чуйковский")
            )
        )
        book1 = bookRepository.save(book1)
        println("\n$book1\n")


        var book2 = Book(
            name = "Евгений Онегин",
            category = BookCategory.School,
            publish = Publish(name = "издательство School"),
            authot = mutableListOf(
                Author(firstName = "Александр", lastName = "Пушкин")
            )
        )
        book2 = bookRepository.save(book2)
        println("\n$book2\n")

        var book3 = Book(
            name = "Война и мир",
            category = BookCategory.School,
            publish = Publish(name = "издательство School"),
            authot = mutableListOf(
                Author(firstName = "Лев", lastName = "Толстой")
            )
        )
        book3 = bookRepository.save(book3)
        println("\n$book3\n")


        val found = bookRepository.findById(book1.id)
        println("\n\n1: $found \n")

        var foundList = bookRepository.findByCategory(BookCategory.School)
        println("\n\nSchool: $foundList \n")

        bookRepository.delete(book2)

        book3.category = BookCategory.Detectives
        bookRepository.save(book3)

        foundList = bookRepository.findByCategory(BookCategory.School)
        println("School: $foundList \n")

    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}
