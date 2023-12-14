package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.entity.Student

@Repository
interface StudentRepo : CrudRepository<Student, Long> {
}