package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.entity.Actor
import ru.sber.springjpademo.persistence.entity.Film
import ru.sber.springjpademo.persistence.entity.Language
import ru.sber.springjpademo.persistence.repository.FilmRepository

@SpringBootApplication
class SpringDataApplication(private val filmRepository: FilmRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {

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
                actors = mutableSetOf(
                    Actor(name = "First Actor"),
                    Actor(name = "Second Actor"),
                    Actor(name = "Third Actor")
                ),
                language = Language(name = "Third Language")
            )

        filmRepository.save(firstFilm)
        filmRepository.save(secondFilm)
        filmRepository.save(thirdFilm)

        var found = filmRepository.findById(firstFilm.film_id)
        println("film found: $found \n")

        val allFilms = filmRepository.findAll()
        println("all films: $allFilms")

        filmRepository.delete(firstFilm)

        secondFilm.language = Language(name = "Third Language")

        filmRepository.save(secondFilm)

    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}
