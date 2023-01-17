package ru.sber.springdata.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springdata.entity.Person

interface PersonRepository : CrudRepository<Person, Long>