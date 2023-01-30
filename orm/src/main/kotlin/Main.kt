import dao.MovieDao
import entity.Actor
import entity.Director
import entity.Genre
import entity.Movie
import org.hibernate.cfg.Configuration
import java.time.LocalDate
import java.time.Month

// Example
fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Actor::class.java)
        .addAnnotatedClass(Movie::class.java)
        .addAnnotatedClass(Director::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = MovieDao(sessionFactory)

        val director1 = Director(
            fullName = "Quentin Tarantino",
            dateOfBirth = LocalDate.of(1953, Month.MARCH, 27),
            movies = mutableSetOf()
        )
        val actor1 = Actor(
            fullName = "John Travolta",
            dateOfBirth = LocalDate.of(1954, Month.FEBRUARY, 18),
            movies = mutableSetOf()
        )
        val actor2 = Actor(
            fullName = "Samuel L. Jackson",
            dateOfBirth = LocalDate.of(1948, Month.DECEMBER, 21),
            movies = mutableSetOf()
        )

        val actor3 = Actor(
            fullName = "Kurt Russell",
            dateOfBirth = LocalDate.of(1951, Month.MARCH, 17),
            movies = mutableSetOf()
        )

        val movie1 = Movie(
            name = "Pulp Fiction",
            releaseYear = 1994,
            genre = Genre.CRIME,
            director = director1,
            actors = mutableSetOf(actor1, actor2)
        )

        val movie2 = Movie(
            name = "The Hateful Eight",
            releaseYear = 2015,
            genre = Genre.WESTERN,
            director = director1,
            actors = mutableSetOf(actor1, actor3)
        )

        dao.save(movie1)
        dao.save(movie2)

        dao.find(movie1.id)?.let { println("Movie found by id: $it") }

        println("All movies: ${dao.findAll()}")
    }
}

