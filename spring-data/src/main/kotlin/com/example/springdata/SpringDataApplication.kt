package com.example.springdata

import com.example.springdata.services.PersonService
import org.springframework.boot.CommandLineRunner
import com.example.springdata.model.Address
import com.example.springdata.model.Passport
import com.example.springdata.model.Person
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


@SpringBootApplication
class SpringDataApplication

fun main(args: Array<String>) {
	runApplication<SpringDataApplication>(*args)
}

@Component
class Runner(
	private val personService: PersonService
) : CommandLineRunner {
	override fun run(vararg args: String) {
		var pavel = Person(
			name = "Pavel",
			age = 24,
			passport = Passport(
				series = "123421",
				number = "243235"
			),
			address = Address(
				city = "Rostov",
				street = "Krupskoy",
				building = "1"
			)
		)

		var roman = Person(
			name = "Roman",
			age = 50,
			passport = Passport(
				series = "544331",
				number = "111111"
			),
			address = Address(
				city = "Rostov",
				street = "Stachki",
				building = "211"
			)
		)

		pavel = personService.create(pavel)
		roman = personService.create(roman)

		val personDb = personService.findById(roman.id!!)

		println("Person roman равен найденному в БД = " + (roman == personDb))

		val updatePavel = pavel.copy(age = 30)

		personService.update(updatePavel)

		val personUpdatePavelDb = personService.findById(pavel.id!!)

		println("Person pavel равен найденному в БД = " + (updatePavel == personUpdatePavelDb))

		personService.delete(pavel.id!!)

		val personDeletePavelDb = personService.findById(pavel.id!!)

		println("Person pavel не найден в БД = $personDeletePavelDb")
	}

}


