package com.firebat.hibernateapp

import com.firebat.hibernateapp.dao.PrincipalDao
import com.firebat.hibernateapp.dao.SchoolDao
import com.firebat.hibernateapp.dao.StudentDao
import com.firebat.hibernateapp.entity.Principal
import com.firebat.hibernateapp.entity.School
import com.firebat.hibernateapp.entity.Student
import com.firebat.hibernateapp.entity.SuccessRate
import org.hibernate.cfg.Configuration

/**
 * Есть ряд школ
 * У каждой из них есть один директор
 * У каждой из них есть множество учеников
 */
fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Principal::class.java)
        .addAnnotatedClass(School::class.java)
        .addAnnotatedClass(Student::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val principalDao = PrincipalDao(sessionFactory)
        val principal1 = Principal(
            firstName = "Виталий",
            lastName = "Витальев",
        )
        val principal2 = Principal(
            firstName = "Сергей",
            lastName = "Сергеев",
            yearsOfRuling = 11
        )
        principalDao.save(principal1)
        principalDao.save(principal2)

        val schoolDao = SchoolDao(sessionFactory)
        val school1 = School(
            name = "Школа №1",
            positionInTheSchoolCompetition = 1,
            principal = principal1,
        )
        val school2 = School(
            name = "Школа №2",
            principal = principal2,
        )
        schoolDao.save(school1)
        schoolDao.save(school2)

        val studentDao = StudentDao(sessionFactory)
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
        studentDao.save(student1)
        studentDao.save(student2)

        val foundPrincipal = principalDao.findByYearsOfRulingMoreThen(10)
        println("===== $foundPrincipal")
        val foundSchool = schoolDao.findByPositionInTheSchoolCompetitionUnknown()
        println("===== $foundSchool")
        val foundStudent = studentDao.findBySuccessRate(SuccessRate.SILVER)
        println("===== $foundStudent")
    }
}