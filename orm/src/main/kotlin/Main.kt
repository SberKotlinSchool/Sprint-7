import entity.Actor
import entity.Film
import entity.Language
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Film::class.java)
        .addAnnotatedClass(Actor::class.java)
        .addAnnotatedClass(Language::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = FilmDAO(sessionFactory)

        val firstFilm =
            Film(
                title = "First Film",
                localTitle = "First Film Local",
                actors = mutableSetOf(Actor(name = "First Actor"), Actor(name = "Second Actor")),
                language = Language(name = "First Language")
            )
        val secondFilm =
            Film(
                title = "Second Film",
                localTitle = "Second Film Local",
                actors = mutableSetOf(Actor(name = "Third Actor")),
                language = Language(name = "Second Language")
            )
        val thirdFilm =
            Film(
                title = "Third Film",
                localTitle = "Third Film Local",
                actors = mutableSetOf(Actor(name = "First Actor"), Actor(name = "Second Actor"), Actor(name = "Third Actor")),
                language = Language(name = "Third Language")
            )

        dao.save(firstFilm)
        dao.save(secondFilm)
        dao.save(thirdFilm)

        var found = dao.find(firstFilm.film_id)
        println("film found: $found \n")

        found = dao.find(secondFilm.title)
        println("film found: $found \n")

        val allFilms = dao.findAll()
        println("all films: $allFilms")

        dao.delete(firstFilm)

        secondFilm.language = Language(name = "Third Language")

        dao.update(secondFilm)
    }
}

class FilmDAO(private val sessionFactory: SessionFactory) {
    fun save(person: Film) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(person)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Film? {
        val result: Film?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Film::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(title: String): Film? {
        val result: Film?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Film::class.java).using("title", title).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Film> {
        val result: List<Film>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Film").list() as List<Film>
            session.transaction.commit()
        }
        return result
    }

    fun delete(film: Film) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(film)
            session.transaction.commit()
        }
    }

    fun update(film: Film) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(film)
            session.transaction.commit()
        }
    }
}