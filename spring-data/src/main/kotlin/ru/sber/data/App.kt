package ru.sber.data


import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.data.db.entity.Actor
import ru.sber.data.db.repository.ActorRepository
import ru.sber.data.db.repository.FilmRepository
import ru.sber.data.db.repository.ProducerRepository

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application(
    private val filmRepository: FilmRepository,
    private val actorRepository: ActorRepository,
    private val producerRepository: ProducerRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        filmRepository.findAll().forEach{ println("${it.name} ${it.rating}")}
        val actor = Actor(name = "Ivan", salary = 137_000)
        actorRepository.save(actor)
        val producer = producerRepository.getById(1)
    }
}