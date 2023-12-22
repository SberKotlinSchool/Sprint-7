package ru.sber.springdata.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.orm.entity.Student

@Repository
interface StudentRepository : CrudRepository<Student, Long> {
    fun findByFullName(fullName: String): List<Student>
}