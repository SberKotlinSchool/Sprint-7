package ru.sber

import org.hibernate.cfg.Configuration
import ru.sber.entity.Address
import ru.sber.entity.Course
import ru.sber.entity.Student
import ru.sber.entity.Teacher
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class Main {
    object HibernateUtil {
        private val entityManagerFactory: EntityManagerFactory by lazy {
            Persistence.createEntityManagerFactory("example")
        }

        fun createEntityManager(): EntityManager {
            return entityManagerFactory.createEntityManager()
        }

        fun close() {
            entityManagerFactory.close()
        }
    }

    fun createSessionFactory(): Configuration {
        return Configuration().configure()
            .addAnnotatedClass(Course::class.java)
            .addAnnotatedClass(Student::class.java)
            .addAnnotatedClass(Teacher::class.java)
            .addAnnotatedClass(Address::class.java)
    }

    fun main() {
        val sessionFactory = createSessionFactory()
        val entityManager = HibernateUtil.createEntityManager()

        // CREATE
        val course = Course(name = "Math")
        val address = Address(street = "street")
        val student = Student(name = "John Doe", course = course, address = address)

        entityManager.transaction.begin()
        entityManager.persist(course)
        entityManager.persist(student)
        entityManager.transaction.commit()

        // READ
        val retrievedCourse = entityManager.find(Course::class.java, course.id)
        println("Course: $retrievedCourse")

        // UPDATE
        entityManager.transaction.begin()
        retrievedCourse?.name = "Physics"
        entityManager.transaction.commit()

        // DELETE
        entityManager.transaction.begin()
        entityManager.remove(retrievedCourse)
        entityManager.transaction.commit()

        entityManager.close()
        HibernateUtil.close()
    }

    private fun Address(street: String): Address {
        TODO("Not yet implemented")
    }
}


