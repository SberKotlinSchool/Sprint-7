package com.homework.orm.dao

import com.homework.orm.entity.Person
import org.hibernate.SessionFactory

class PersonDao (
    private val sessionFactory: SessionFactory
        ) {
    fun save(person: Person) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(person)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Person? {
        val person: Person?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            person = session.get(Person::class.java, id)
            session.transaction.commit()
        }
        return person
    }

    fun findAll(): List<Person> {
        val persons: List<Person>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            persons = session.createQuery("from Person", Person::class.java).list() as List<Person>
            session.transaction.commit()
        }
        return persons
    }

    fun delete(person: Person) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(person)
            session.transaction.commit()
        }
    }

    fun update(person: Person) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(person)
            session.transaction.commit()
        }
    }
}