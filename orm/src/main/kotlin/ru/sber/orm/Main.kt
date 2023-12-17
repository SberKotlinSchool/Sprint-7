package ru.sber.orm

import org.hibernate.cfg.Configuration
import ru.sber.orm.dao.BookDAO
import ru.sber.orm.entity.Author
import ru.sber.orm.entity.Book
import ru.sber.orm.entity.Library

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Author::class.java)
        .addAnnotatedClass(Library::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = BookDAO(sessionFactory)

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

        dao.save(book1)
        dao.save(book2)
        dao.save(book3)

        var found = dao.find(book1.id)
        println("One: $found \n")

        found = dao.find(book2.title)
        println("Two: $found \n")

        book1.title = "${book1.title} 2"
        var updated = dao.update(book1)
        println("Three: $updated \n")

        dao.delete(book2)

        val allBooks = dao.findAll()
        println("All: $allBooks")
    }
}