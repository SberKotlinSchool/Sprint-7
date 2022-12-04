package ru.sber.orm.dao


import org.hibernate.SessionFactory
import ru.sber.orm.models.Book

class BookDao(
    private val sessionFactory: SessionFactory
) {
    fun save(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(book)
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

    fun findAll(): List<Book> {
        val result: List<Book>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from book", Book::class.java).list() as List<Book>
            session.transaction.commit()
        }
        return result
    }

    fun delete(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(book)
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
}
