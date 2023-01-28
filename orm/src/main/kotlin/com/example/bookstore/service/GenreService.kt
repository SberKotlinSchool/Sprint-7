package com.example.bookstore.service

import com.example.bookstore.entity.Genre
import org.hibernate.SessionFactory

class GenreService (private val sessionFactory: SessionFactory)
{
    fun fetchAll(): List<Genre> {
        val result: List<Genre>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Genre").list() as List<Genre>
            session.transaction.commit()
        }
        return result
    }

    fun findById(id: Long): Genre? {
        val genre: Genre?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            genre = session.get(Genre::class.java, id)
            session.transaction.commit()
        }
        return genre
    }

    fun findByName(name: String): Genre? {
        val genre: Genre?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("from Genre where name = :name ")
                .setParameter("name", name)
            val list = query.list()
            genre =  list.get(0) as Genre
            session.transaction.commit()
        }
        return genre
    }


    fun save(genre: Genre) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(genre)
            session.transaction.commit()
        }
    }
}