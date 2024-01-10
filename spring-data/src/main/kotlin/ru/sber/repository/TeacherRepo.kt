package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.entity.Teacher

@Repository
interface TeacherRepo : CrudRepository<Teacher, Long> {
}