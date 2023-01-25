package ru.sber.comdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.comdata.persistence.entities.Identity

@Repository
interface  IdentityRepository : CrudRepository<Identity, Long> {
    fun findByName(name: String): Identity?
}