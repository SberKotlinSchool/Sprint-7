package ru.sber

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import ru.sber.enteties.Author
import ru.sber.enteties.Book
import ru.sber.enteties.BookCategory
import ru.sber.enteties.Publish

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Book::class.java)
        .addAnnotatedClass(Publish::class.java)
        .addAnnotatedClass(Author::class.java)
        .buildSessionFactory()

    sessionFactory.use { session ->
        val dao = BookDAO(session)

        val book1 = Book(
            name = "Сказки",
            category = BookCategory.Story,
            publish = Publish(name = "издательство сказок"),
            authot = mutableListOf(
                Author(firstName = "Александр", lastName = "Пушкин"),
                Author(firstName = "Корней", lastName = "Чуйковский")
            )
        )
        dao.save(book1)

        val book2 = Book(
            name = "Евгений Онегин",
            category = BookCategory.School,
            publish = Publish(name = "издательство School"),
            authot = mutableListOf(
                Author(firstName = "Александр", lastName = "Пушкин")
            )
        )
        dao.save(book2)

        val book3 = Book(
            name = "Война и мир",
            category = BookCategory.School,
            publish = Publish(name = "издательство School"),
            authot = mutableListOf(
                Author(firstName = "Лев", lastName = "Толстой")
            )
        )
        dao.save(book3)


        val found = dao.find(book1.id)
        println("1: $found \n")

        var foundList = dao.find(BookCategory.School)
        println("School: $foundList \n")

        dao.delete(book2.id)

        book3.category = BookCategory.Detectives
        dao.update(book3)

        foundList = dao.find(BookCategory.School)
        println("School: $foundList \n")
    }
}

class BookDAO(private val sessionFactory: SessionFactory) {
    fun save(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(book)
            session.transaction.commit()
        }
    }

    fun update(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(book)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Book? {
        val result: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Book::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(category: BookCategory): List<Book>? {
        val result: MutableList<Book>?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Book where category = :category").setParameter("category", category)
                .list() as MutableList<Book>?
            session.transaction.commit()
        }
        return result
    }

    fun delete(id: Long) {
        val book: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            book = find(id)
            session.delete(book)
            session.transaction.commit()
        }
    }
}