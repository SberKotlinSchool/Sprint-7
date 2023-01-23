package ru.sber.addressbook.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.sber.addressbook.model.Person
import ru.sber.addressbook.repository.PersonRepository

@Service
class PersonService @Autowired constructor(val personRepository: PersonRepository) {

    fun getAll() = personRepository.findAll()

    fun getById(id: Long): Person? = personRepository.findById(id).get()

    fun add(person: Person) {
        personRepository.save(person)
    }

    fun delete(id: Long) {
        personRepository.deleteById(id)
    }

    fun update(person: Person) {
        return personRepository.updatePersonById(person.id!!, person.name, person.city)
    }
}