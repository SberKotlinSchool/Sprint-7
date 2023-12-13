package com.example.sprint6.hibernate.repository

import com.example.sprint6.hibernate.model.Book
import org.hibernate.SessionFactory

class BookRepository(private val sessionFactory: SessionFactory) {
    fun save(book: Book) = sessionFactory.openSession().use { session ->
        session.beginTransaction()
        session.save(book)
        session.transaction.commit()
    }

    fun get(id: Long) = sessionFactory.openSession().use { session ->
        session.beginTransaction()
        val result: Book = session.get(Book::class.java, id)
        session.transaction.commit()
        result
    }

    fun update(book: Book) =
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val result: Book = session.merge(book) as Book
            session.transaction.commit()
            result
        }

    fun delete(book: Book) = sessionFactory.openSession().use { session ->
        session.beginTransaction()
        session.delete(book)
        session.transaction.commit()
    }
}
