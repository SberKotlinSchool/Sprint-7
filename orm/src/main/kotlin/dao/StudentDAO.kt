package dao

import entity.Student
import org.hibernate.SessionFactory

class StudentDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction().let { tx ->
                try {
                    session.save(student)
                    tx.commit()
                } catch (e: Exception) {
                    tx.rollback()
                }
            }
        }
    }

    fun find(id: Long): Student? {
        val result: Student?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Student::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(email: String): Student? {
        val result: Student?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result =
                session.byNaturalId(Student::class.java).using("email", email).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Student> {
        var result: List<Student>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Student").list() as List<Student>
            session.transaction.commit()
        }
        return result
    }

    fun deleteAll() {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.createQuery("delete from Student").executeUpdate()
            session.createQuery("delete from HomeAddress").executeUpdate()
            session.transaction.commit()
        }
    }
}