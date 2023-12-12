package com.example.springdata.repositories

import com.example.springdata.model.Person
import org.springframework.data.repository.CrudRepository

interface PersonRepository: CrudRepository<Person, Int>