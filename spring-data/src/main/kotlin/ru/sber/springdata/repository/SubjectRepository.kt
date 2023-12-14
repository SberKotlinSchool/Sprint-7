package ru.sber.springdata.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.orm.entities.Subject
import java.util.*

@Repository
interface SubjectRepository: CrudRepository<Subject, Long> {
    fun findAllById(id: Long): List<Subject>
}