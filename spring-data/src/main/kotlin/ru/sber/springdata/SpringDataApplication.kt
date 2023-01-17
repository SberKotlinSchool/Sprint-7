package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.entity.Passport
import ru.sber.springdata.entity.Person
import ru.sber.springdata.repository.PassportRepository
import ru.sber.springdata.repository.PersonRepository

@SpringBootApplication
class SpringDataApplication(
    private val personRepository: PersonRepository,
    private val passportRepository: PassportRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {

    val person = Person(
        surname = "Ivanov",
        firstname = "Ivan",
        passport = null
    )
    val savedPerson = personRepository.save(person)
        println("Saved person: $savedPerson")

        val personfindById = personRepository.findById(savedPerson.id)
        println("Founded person: $personfindById")

        val passport = Passport(person = null)
        val savedPassport = passportRepository.save(passport)
        println("Saved passport: $savedPassport")

        person.passport = savedPassport
        val savedPersonWithPassport = personRepository.save(person)
        println("Saved person with passport: $savedPersonWithPassport")

    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}
