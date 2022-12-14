package com.homework.springdata

import com.homework.springdata.entity.Car
import com.homework.springdata.entity.CarDetails
import com.homework.springdata.entity.CarInsurance
import com.homework.springdata.entity.CarPassport
import com.homework.springdata.entity.Person
import com.homework.springdata.repository.CarInsuranceRepository
import com.homework.springdata.repository.CarPassportRepository
import com.homework.springdata.repository.CarRepository
import com.homework.springdata.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Application (
    @Autowired
    private val personRepository: PersonRepository,
    @Autowired
    val carRepository: CarRepository,
    @Autowired
    val carPassportRepository: CarPassportRepository,
    @Autowired
    val carInsuranceRepository: CarInsuranceRepository,
        ) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val person1 = Person(firstName = "fName1", lastName = "lName1", phoneNumber = "123")
        val person2 = Person(firstName = "fName2", lastName = "lName2", phoneNumber = "456")
        val car1 = Car(details = CarDetails("red","Mozeratti"))
        val passport1 = CarPassport(car = car1, owner = person1)
        val insurance1 = CarInsurance(car = car1, drivers = mutableListOf(person1,person2) )
        personRepository.save(person1)
        personRepository.save(person2)
        carRepository.save(car1)
        carPassportRepository.save(passport1)
        carInsuranceRepository.save(insurance1)

        val personOptional = personRepository.findAll()
        println("Found Person : ${personOptional.firstOrNull()}")

        val carOptional = carRepository.findAll()
        println("Found Car : ${carOptional.firstOrNull()}")

        val passportOptional = carPassportRepository.findAll()
        println("Found Passport : ${passportOptional.firstOrNull()}")

        val insuranceOptional = carInsuranceRepository.findAll()
        println("Found Insurance : ${insuranceOptional.firstOrNull()}")
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}