package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entities.Pilot


class PilotsDao(private val sessionFactory: SessionFactory) {

    fun save(pilot: Pilot) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(pilot)
            session.transaction.commit()
        }
    }

    fun find(id: Long?): Pilot? {
        val result: Pilot?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Pilot::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(name: String): Pilot? {
        val result: Pilot?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Pilot::class.java)
                .using("name", name)
                .loadOptional()
                .orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Pilot>{
        val result: List<Pilot>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("select * from pilot")
                .list() as List<Pilot>
        }
        return result
    }

}