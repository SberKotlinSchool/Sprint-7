package ru.sber.astafex.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.astafex.springdata.entity.Genre
import ru.sber.astafex.springdata.repository.ActorRepository
import ru.sber.astafex.springdata.repository.MovieRepository

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application(
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository
) :
    CommandLineRunner {
    override fun run(vararg args: String?) {
        /*
            val director1 = Director(fullName = "Quentin Tarantino", dateOfBirth = LocalDate.of(1953, Month.MARCH, 27), movies = mutableSetOf())
            val director2 = Director(fullName = "Martin Scorsese", dateOfBirth = LocalDate.of(1942, Month.NOVEMBER, 17), movies = mutableSetOf())

            val actor1 = Actor(fullName = "John Travolta", dateOfBirth = LocalDate.of(1954, Month.FEBRUARY, 18), movies = mutableSetOf())
            val actor2 = Actor(fullName = "Samuel L. Jackson", dateOfBirth = LocalDate.of(1948, Month.DECEMBER, 21), movies = mutableSetOf())
            val actor3 = Actor(fullName = "Kurt Russell", dateOfBirth = LocalDate.of(1951, Month.MARCH, 17), movies = mutableSetOf())
            val actor4 = Actor(fullName = "Robert De Niro", dateOfBirth = LocalDate.of(1943, Month.AUGUST, 17), movies = mutableSetOf())
            val actor5 = Actor(fullName = "Ray Liotta", dateOfBirth = LocalDate.of(1954, Month.DECEMBER, 18), movies = mutableSetOf())

            val movie1 = Movie(name = "Pulp Fiction", releaseYear = 1994, genre = Genre.CRIME, director = director1, actors = mutableSetOf(actor1, actor2))
            val movie2 = Movie(name = "The Hateful Eight", releaseYear = 2015, genre = Genre.WESTERN, director = director1, actors = mutableSetOf(actor1, actor3))
            val movie3 = Movie(name = "Goodfellas", releaseYear = 1990, genre = Genre.CRIME, director = director2, actors = mutableSetOf(actor4, actor5))

            movieRepository.saveAll(
                listOf(movie1, movie2, movie3)
        )*/

        println(movieRepository.findByGenre(Genre.CRIME))

        println(actorRepository.findByFullName("Ray Liotta"))
    }
}