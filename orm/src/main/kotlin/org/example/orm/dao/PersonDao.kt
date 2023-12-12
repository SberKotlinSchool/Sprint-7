package org.example.orm.dao

import org.example.orm.model.Person

interface PersonDao {
    fun create(person: Person)
    fun update(person: Person)
    fun findById(id: Int): Person?
    fun delete(id: Int)
}