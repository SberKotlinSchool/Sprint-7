package com.example.bookstore.service

import com.example.bookstore.entity.Book
import org.hibernate.SessionFactory

class BookService (private val sessionFactory: SessionFactory)
{
    fun fetchAll(): List<Book> {
        val result: List<Book>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Book").list() as List<Book>
            session.transaction.commit()
        }
        return result
    }

    fun findById(id: Long): Book? {
        val book: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            book = session.get(Book::class.java, id)
            session.transaction.commit()
        }
        return book
    }

    fun findByName(name: String): Book? {
        val book: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("from Book where name = :name ")
                .setParameter("name", name)
            val list = query.list()
            book =  list.get(0) as Book
            session.transaction.commit()
        }
        return book
    }

    fun findByAuthorId(authorId: Long): Book? {
        val book: Book?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("from Book where authorId = :authorId ")
                .setParameter("authorId", authorId)
            val list = query.list()
            book =  list.get(0) as Book
            session.transaction.commit()
        }
        return book
    }

    fun save(book: Book) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(book)
            session.transaction.commit()
        }
    }
}