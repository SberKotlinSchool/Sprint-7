package com.homework.orm.dao

import com.homework.orm.entity.CarPassport
import org.hibernate.SessionFactory

class CarPassportDao (
    private val sessionFactory: SessionFactory
        ) {
    fun save(passport: CarPassport) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(passport)
            session.transaction.commit()
        }
    }

    fun find(id: Long): CarPassport? {
        val passport: CarPassport?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            passport = session.get(CarPassport::class.java, id)
            session.transaction.commit()
        }
        return passport
    }

    fun findAll(): List<CarPassport> {
        val passports: List<CarPassport>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            passports = session.createQuery("from CarPassport", CarPassport::class.java).list() as List<CarPassport>
            session.transaction.commit()
        }
        return passports
    }

    fun delete(passport: CarPassport) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(passport)
            session.transaction.commit()
        }
    }

    fun update(passport: CarPassport) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(passport)
            session.transaction.commit()
        }
    }
}