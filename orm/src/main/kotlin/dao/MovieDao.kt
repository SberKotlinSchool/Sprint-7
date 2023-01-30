package dao

import entity.Movie
import org.hibernate.SessionFactory

class MovieDao(private val sessionFactory: SessionFactory) {
    fun save(movie: Movie) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(movie)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Movie? {
        val result: Movie?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Movie::class.java, id)
            session.transaction.commit()
        }
        return result
    }


    fun findAll(): List<Movie> {
        val result: List<Movie>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Movie").list() as List<Movie>
            session.transaction.commit()
        }
        return result
    }
}