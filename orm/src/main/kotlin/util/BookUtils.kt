package util

import entities.Book
import org.hibernate.SessionFactory


class BookUtils(
    private val sessionFactory: SessionFactory,
) {
    fun add(book: Book) {
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

    fun find(name: String): Book? {
        val result: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Book::class.java)
                .using("name", name)
                .loadOptional()
                .orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun update(book: Book): Book {
        val result: Book
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.merge(book) as Book
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

    fun findAll(): List<Book> {
        val result: List<Book>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Book").list() as List<Book>
            session.transaction.commit()
        }
        return result
    }
}