package com.homework.springdata

import com.homework.springdata.dao.CarInsuranceDao
import com.homework.springdata.dao.CarPassportDao
import com.homework.springdata.dao.PersonDao
import com.homework.springdata.entity.Car
import com.homework.springdata.entity.CarDetails
import com.homework.springdata.entity.CarInsurance
import com.homework.springdata.entity.CarPassport
import com.homework.springdata.entity.Person
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Person::class.java)
        .addAnnotatedClass(CarInsurance::class.java)
        .addAnnotatedClass(CarPassport::class.java)
        .addAnnotatedClass(Car::class.java)
        .buildSessionFactory()

    sessionFactory.use {
        val carPassportDao = CarPassportDao(sessionFactory)
        val carInsuranceDao = CarInsuranceDao(sessionFactory)
        val personDao = PersonDao(sessionFactory)

        val person1 = Person(firstName = "fName1", lastName = "lName1", phoneNumber = "123")
        val person2 = Person(firstName = "fName2", lastName = "lName2", phoneNumber = "456")
        val car1 = Car(details = CarDetails("red","Mozeratti"))
        val passport1 = CarPassport(car = car1, owner = person1)
        val insurance1 = CarInsurance(car = car1, drivers = mutableListOf(person1,person2) )
        car1.passport = passport1
        car1.insurance = insurance1

        carPassportDao.save(passport1)
        carInsuranceDao.save(insurance1)

        var found = personDao.find(person1.personId)
        println("по id ${person1.personId} Найден водитель : $found")

        found = personDao.find(person2.personId)
        println("по id ${person2.personId} Найден водитель : $found")

        val allPersons = personDao.findAll()
        println("Все водители : $allPersons")


        val insurance = carInsuranceDao.find(insurance1.insuranceId)
        println("по id ${insurance1.insuranceId} Найдена страховка : $insurance")

        val passport = carPassportDao.find(passport1.passportId)
        println("по id ${passport1.passportId} Найден ПТС : $passport")
    }
}