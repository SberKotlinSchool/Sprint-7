package org.example.orm.dao

import org.example.orm.model.Person
import org.hibernate.SessionFactory
import org.springframework.stereotype.Component

@Component
class PersonDaoImpl(
    val sessionFactory: SessionFactory
): PersonDao {
    override fun create(person: Person) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(person)
            session.transaction.commit()
        }
    }

    override fun update(person: Person) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(person)
            session.transaction.commit()
        }
    }

    override fun findById(id: Int): Person? {
        val result: Person?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Person::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    override fun delete(id: Int) {
        val person: Person?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            person = findById(id)
            session.delete(person)
            session.transaction.commit()
        }
    }
}