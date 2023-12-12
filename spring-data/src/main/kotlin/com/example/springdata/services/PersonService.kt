package com.example.springdata.services

import com.example.springdata.model.Person

interface PersonService {
    fun create(person: Person): Person
    fun findById(id: Int): Person?
    fun update(person: Person)
    fun delete(id: Int): Person
}