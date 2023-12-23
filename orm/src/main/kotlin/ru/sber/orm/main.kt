package ru.sber.orm

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import ru.sber.orm.entities.Student
import ru.sber.orm.entities.Subject
import  ru.sber.orm.entities.Teacher

fun main() {

    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Student::class.java)
        .addAnnotatedClass(Teacher ::class.java)
        .addAnnotatedClass(Subject ::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = SubjectDao(sessionFactory)

        val teacher1 = Teacher( name = "teacher1 teacherovich", age = 50 )
        val teacher2 = Teacher( name = "teacher2 teacherovich", age = 65 )
        val teacher3 = Teacher( name = "teacher3 teacherovich", age = 54 )

        val student1 = Student( name = "student1", email = "student1@mail.ru")
        val student2 = Student( name = "student2", email = "student2@mail.ru")
        val student3 = Student( name = "student3", email = "student3@mail.ru")
        val student4 = Student( name = "student4", email = "student4@mail.ru")

        val subject1 = Subject(
            name = "subject1",
            teacher = teacher1,
            students = mutableListOf(student1, student2 ) )

        val subject2 = Subject(
            name = "subject2",
            teacher = teacher2,
            students = mutableListOf(student2, student3 ) )

        val subject3 = Subject(
            name = "subject3",
            teacher = teacher3,
            students = mutableListOf(student1, student2, student4 ) )

        val subject4 = Subject(
            name = "subject4",
            teacher = teacher1,
            students = mutableListOf(student1, student2, student3, student4 ) )

        dao.save(subject1)
        dao.save(subject2)
        dao.save(subject3)
        dao.save(subject4)

        println(dao.find(subject3.id))

        println(dao.findAll())

    }

}

class SubjectDao( private val sessionFactory: SessionFactory ){

    fun save(subject: Subject) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(subject)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Subject? {
        val result: Subject?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Subject::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Subject>{
        val result: List<Subject>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Subject").list() as List<Subject>
            session.transaction.commit()
        }
        return result


    }

}