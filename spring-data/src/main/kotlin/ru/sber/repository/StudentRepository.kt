package ru.sber.repository

import ru.sber.enteties.Student
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : CrudRepository<Student, Long> {
    fun findAllById(id: Long): List<Student>
}