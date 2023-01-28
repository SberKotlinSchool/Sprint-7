package com.example.bookstore.service;

import com.example.bookstore.entity.Author
import org.hibernate.SessionFactory

class AuthorService (private val sessionFactory: SessionFactory)
{
    fun fetchAll(): List<Author> {
        val result: List<Author>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Author").list() as List<Author>
            session.transaction.commit()
        }
        return result
    }

    fun findById(id: Long): Author? {
        val author: Author?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            author = session.get(Author::class.java, id)
            session.transaction.commit()
        }
        return author
    }

    fun findByName(name: String): Author? {
        val author: Author?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("from Author where firstName = :name ")
                .setParameter("name", name)
            val list = query.list()
            author =  list.get(0) as Author
            session.transaction.commit()
        }
        return author
    }

    fun save(author: Author) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(author)
            session.transaction.commit()
        }
    }
}