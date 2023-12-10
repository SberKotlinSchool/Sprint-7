package com.firebat.hibernateapp.repository

import com.firebat.hibernateapp.entity.Student
import com.firebat.hibernateapp.entity.SuccessRate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : CrudRepository<Student, Long> {
    fun findBySuccessRate(successRate: SuccessRate): List<Student>
}