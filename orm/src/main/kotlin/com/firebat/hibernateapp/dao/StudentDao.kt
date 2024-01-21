package com.firebat.hibernateapp.dao

import com.firebat.hibernateapp.entity.Student
import com.firebat.hibernateapp.entity.SuccessRate
import org.hibernate.SessionFactory

class StudentDao(private val sessionFactory: SessionFactory) {
    fun save(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(student)
            session.transaction.commit()
        }
    }

    fun findById(id: Long): Student? {
        val result: Student?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Student::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findBySuccessRate(successRate: SuccessRate): List<Student> {
        val result: List<Student>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("FROM Student WHERE successRate = :successRate")
            query.setParameter("successRate", successRate)
            result = query.resultList as List<Student>
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Student> {
        val result: List<Student>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("FROM Student").list() as List<Student>
            session.transaction.commit()
        }
        return result
    }

    fun update(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(student)
            session.transaction.commit()
        }
    }

    fun delete(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(student)
            session.transaction.commit()
        }
    }
}