package ru.sber.orm.dao

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ru.sber.orm.models.Author
import ru.sber.orm.models.Book
import ru.sber.orm.models.Genre


internal class BookDaoTest {

    private fun getSessionFactory(): SessionFactory {
        return Configuration().configure()
            .addAnnotatedClass(BookDao::class.java)
            .addAnnotatedClass(Book::class.java)
            .addAnnotatedClass(Author::class.java)
            .addAnnotatedClass(Genre::class.java)
            .buildSessionFactory()

    }

    @Test
    fun testSaveSuccessfull() {
        val sessionFactory = getSessionFactory()

        sessionFactory.use { sessionFactory ->
            val dao = BookDao(sessionFactory)

            val book = Book(
                name = "Ночной дозор",
                author = Author(firstName = "Сергей", secondName = "Васильевич", lastName = "Лукьяненко"),
                genre = Genre(name = "Фентези")
            )
            dao.save(book)
            val foundBook = dao.find(book.id)
            assertNotNull(foundBook)
            Assertions.assertEquals(book.name, foundBook?.name)
            Assertions.assertEquals(book.author?.firstName, foundBook?.author?.firstName)
            Assertions.assertEquals(book.author?.secondName, foundBook?.author?.secondName)
            Assertions.assertEquals(book.author?.lastName, foundBook?.author?.lastName)
            Assertions.assertEquals(book.genre?.name, foundBook?.genre?.name)
        }
    }
}