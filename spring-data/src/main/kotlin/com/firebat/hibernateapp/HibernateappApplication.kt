package com.firebat.hibernateapp

import com.firebat.hibernateapp.entity.Principal
import com.firebat.hibernateapp.entity.School
import com.firebat.hibernateapp.entity.Student
import com.firebat.hibernateapp.entity.SuccessRate
import com.firebat.hibernateapp.repository.PrincipalRepository
import com.firebat.hibernateapp.repository.SchoolRepository
import com.firebat.hibernateapp.repository.StudentRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HibernateappApplication(
    private val principalRepository: PrincipalRepository,
    private val schoolRepository: SchoolRepository,
    private val studentRepository: StudentRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val principal1 = Principal(
            firstName = "Виталий",
            lastName = "Витальев",
        )
        val principal2 = Principal(
            firstName = "Сергей",
            lastName = "Сергеев",
            yearsOfRuling = 11
        )
        principalRepository.saveAll(listOf(principal1, principal2))

        val school1 = School(
            name = "Школа №1",
            positionInTheSchoolCompetition = 1,
            principal = principal1,
        )
        val school2 = School(
            name = "Школа №2",
            principal = principal2,
        )
        schoolRepository.saveAll(listOf(school1, school2))

        val student1 = Student(
            firstName = "Андрей",
            lastName = "Андреев",
            successRate = SuccessRate.GOLD,
            school = school1
        )
        val student2 = Student(
            firstName = "Родион",
            lastName = "Родионов",
            school = school1
        )
        studentRepository.saveAll(listOf(student1, student2))

        val foundPrincipal = principalRepository.findByYearsOfRulingGreaterThan(10)
        println("===== $foundPrincipal")
        val foundSchool = schoolRepository.findByPositionInTheSchoolCompetitionIsNull()
        println("===== $foundSchool")
        val foundStudent = studentRepository.findBySuccessRate(SuccessRate.SILVER)
        println("===== $foundStudent")
    }
}

fun main(args: Array<String>) {
    runApplication<HibernateappApplication>(*args)
}
