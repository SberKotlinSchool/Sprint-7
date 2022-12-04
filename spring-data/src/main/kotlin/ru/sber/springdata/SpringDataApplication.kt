package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.Repositories.ClientRepository

@SpringBootApplication
class SpringDataApplication(
	private val clientRepository: ClientRepository
) : CommandLineRunner {
	override fun run(vararg args: String?) {

	}
}

fun main(args: Array<String>) {
	runApplication<SpringDataApplication>(*args)
}

