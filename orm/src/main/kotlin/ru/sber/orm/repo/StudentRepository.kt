package ru.sber.orm.repo

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Student

class StudentRepository(private val sessionFactory: SessionFactory) {
    fun create(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(session)
            session.transaction.commit()
        }
    }

    fun getById(id: Long): Student? {
        val result: Student?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Student::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun update(student: Student): Student {
        val result: Student
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.merge(session) as Student
            session.transaction.commit()
        }
        return result
    }

    fun delete(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(student)
            session.transaction.commit()
        }
    }
}