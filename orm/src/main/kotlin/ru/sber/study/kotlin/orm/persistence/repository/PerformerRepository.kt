package ru.sber.study.kotlin.orm.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.study.kotlin.orm.persistence.entities.Performer

@Repository
interface PerformerRepository : CrudRepository<Performer, Long> {
    fun findByName(name: String): Performer?
}