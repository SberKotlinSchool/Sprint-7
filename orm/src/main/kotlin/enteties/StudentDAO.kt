package enteties

import org.hibernate.SessionFactory

class StudentDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(student)
            session.transaction.commit()
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
        val result: List<Student>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Student").list() as List<Student>
            session.transaction.commit()
        }
        return result
    }

    fun delete(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            student.university = University(name="")
            session.update(student)
            session.delete(student)
            session.transaction.commit()
        }
    }

    fun update(student: Student) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(student)
            session.transaction.commit()
        }
    }
}