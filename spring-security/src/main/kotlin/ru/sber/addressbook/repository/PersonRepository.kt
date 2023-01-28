package ru.sber.addressbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sber.addressbook.model.Person

@Repository
interface PersonRepository : JpaRepository<Person, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Person p SET p.name = :name, p.city = :city WHERE p.id = :id")
    fun updatePersonById(@Param("id") id: Long, @Param("name") name: String, @Param("city") city: String)
}