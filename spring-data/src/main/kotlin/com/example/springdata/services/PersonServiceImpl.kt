package com.example.springdata.services

import com.example.springdata.repositories.PersonRepository
import com.example.springdata.model.Person
import org.springframework.stereotype.Service
import java.sql.SQLException
import kotlin.jvm.optionals.getOrNull

@Service
class PersonServiceImpl(
    private val personRepository: PersonRepository
) : PersonService {
    override fun create(person: Person): Person {
        return personRepository.save(person)
    }

    override fun findById(id: Int): Person? {
        return personRepository.findById(id).getOrNull()
    }

    override fun update(person: Person) {
        person.id?.let {
            val personDb = findById(it)
            personDb?.let {
                personRepository.save(person)
            } ?: throw SQLException("Person not found")
        } ?: throw Exception("Id can't be NULL")

    }

    override fun delete(id: Int): Person {
        val personDb = findById(id)
        personDb?.let {
            personRepository.deleteById(id)
        } ?: throw SQLException("Person not found")
        return personDb
    }
}