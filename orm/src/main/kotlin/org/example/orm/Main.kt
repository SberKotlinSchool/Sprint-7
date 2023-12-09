package org.example.orm

import org.example.orm.configuration.HibernateConf
import org.example.orm.dao.PersonDao
import org.example.orm.model.Address
import org.example.orm.model.Passport
import org.example.orm.model.Person
import org.springframework.context.annotation.AnnotationConfigApplicationContext


fun main() {
    val context = AnnotationConfigApplicationContext(HibernateConf::class.java)
    val personDao = context.getBean(PersonDao::class.java)

    val pavel = Person(
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

    val roman = Person(
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
    personDao.create(pavel)
    personDao.create(roman)

    val personDb = personDao.findById(roman.id!!)

    println("Person roman равен найденному в БД = " + (roman == personDb))

    val updatePavel = pavel.copy(age = 30)

    personDao.update(updatePavel)

    val personUpdatePavelDb = personDao.findById(pavel.id!!)

    println("Person pavel равен найденному в БД = " + (updatePavel == personUpdatePavelDb))

    personDao.delete(pavel.id)

    val personDeletePavelDb = personDao.findById(pavel.id)

    println("Person pavel не найден в БД = $personDeletePavelDb")

}