package ru.sber.mvc.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.mvc.models.Person

@Repository
interface  PersonRepository : JpaRepository<Person, Long> {
    fun findByUsername(username: String): Person?
}