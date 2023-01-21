package ru.sber.springjpademo.persistence.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sber.springjpademo.persistence.entities.Student

@Repository
interface StudentRepository : CrudRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.name = :name")
    fun findName(@Param("name") name: String): List<Student>

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.email = :email WHERE s = :student")
    fun updateEmail(@Param("student") student: Student, @Param("email") email: String)

    @Transactional
    @Modifying
    @Query("delete Student s WHERE s = :student")
    fun deleteStudent(@Param("student") student: Student)
}