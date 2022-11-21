package ru.sber.spring.jpa

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.spring.jpa.repository.DroneRepository

@SpringBootApplication
class SpringJpaApplication(private val droneRepository: DroneRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {

    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaApplication>(*args)
}
