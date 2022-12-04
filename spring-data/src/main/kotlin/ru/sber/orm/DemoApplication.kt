package ru.sber.orm



import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.orm.models.Author
import ru.sber.orm.models.Book
import ru.sber.orm.models.Genre
import ru.sber.orm.models.BookRepository

@SpringBootApplication
class DemoApplication(private val repository: BookRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val book1 = Book(
            name = "Ночной дозор",
            author = Author(firstName = "Сергей", secondName = "Васильевич", lastName = "Лукьяненко"),
            genre = Genre(name = "Фентези")
        )
        val book2 = Book(
            name = "Войно и мир",
            author = Author(firstName = "Лев", secondName = "Николаевич", lastName = "Толстой"),
            genre = Genre(name = "Роман")
        )

        repository.save(book1)

        repository.save(book2)


        val foundBook1 = repository.findById(book1.id)
        println("Найдена книга: $foundBook1")


        val foundBooks = repository.findAll()
        println("Все книги: $foundBooks")
    }

}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}