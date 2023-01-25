package ru.sber.springjpademo.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.springjpademo.persistence.entity.Student
import java.util.Optional

interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Optional<Student>
}