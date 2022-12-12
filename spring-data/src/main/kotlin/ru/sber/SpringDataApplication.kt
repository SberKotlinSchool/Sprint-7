package ru.sber

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.entities.Language
import ru.sber.entities.Person
import ru.sber.entities.Programmer
import ru.sber.entities.Sex
import ru.sber.repo.ProgrammerRepository
import java.lang.NullPointerException
import javax.transaction.Transactional

@SpringBootApplication
class SpringDataApplication(private val repo: ProgrammerRepository) : CommandLineRunner {

	@Transactional
	override fun run(vararg args: String?) {
		var programmer = Programmer(
			person = Person(name="Сидоров Сидор Сидорович", sex = Sex.M),
			languages = mutableListOf(Language(name="c++"))
		)
		println("before saving there are ${repo.findAll().count()} records in repository")

		repo.save(programmer)
		println("programmer ${programmer.person.name} saved with id=${programmer.id}")

		println("after saving there are ${repo.findAll().count()} records in repository")

		programmer = repo.findById(programmer.id)
			.orElseThrow({ NullPointerException("programmer with id=${programmer.id} not found")})
		println("found programmer with id = ${programmer.id}")

		repo.delete(programmer)
		println("programmer id={$programmer.id} deleted")
	}
}

fun main(args: Array<String>) {
	runApplication<SpringDataApplication>(*args)
}
