package ru.sber.orm

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.orm.entities.Author
import ru.sber.orm.entities.Book
import ru.sber.orm.entities.Genre
import ru.sber.orm.repository.AuthorRepository
import ru.sber.orm.repository.BookRepository
import ru.sber.orm.repository.GenreRepository
import java.time.LocalDateTime

@SpringBootApplication
 open class SpringJpaDemoApplication(
    private val bookRepo: BookRepository,
    private val authorRepo: AuthorRepository,
    private val genreRepo: GenreRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val genre = genreRepo.save(Genre(301, "Thriller"))
        val genre2 = genreRepo.save(Genre(302, "Drama"))
        val author = authorRepo.save(Author(401, "Tolstoy"))
        val author2 = authorRepo.save(Author(402, "Pushkin"))
        bookRepo.save(Book(501, "Anna Karenina", author, "4", genre))
        bookRepo.save(Book(502, "Captain's Daughter", author2, "5", genre2))

        println(bookRepo.getByAuthorName("Tolstoy"))
        println(bookRepo.getByGenreName("Drama"))
        println(bookRepo.getByTitle("Anna Karenina"))
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
