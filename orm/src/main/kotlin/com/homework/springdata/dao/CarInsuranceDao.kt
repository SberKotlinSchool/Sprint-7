package com.homework.springdata.dao

import com.homework.springdata.entity.CarInsurance
import org.hibernate.SessionFactory

class CarInsuranceDao (
    private val sessionFactory: SessionFactory
        ) {
    fun save(insurance: CarInsurance) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(insurance)
            session.transaction.commit()
        }
    }

    fun find(id: Long): CarInsurance? {
        val insurance: CarInsurance?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            insurance = session.get(CarInsurance::class.java, id)
            session.transaction.commit()
        }
        return insurance
    }

    fun findAll(): List<CarInsurance> {
        val insurances: List<CarInsurance>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            insurances = session.createQuery("from CarInsurance", CarInsurance::class.java).list() as List<CarInsurance>
            session.transaction.commit()
        }
        return insurances
    }

    fun delete(insurance: CarInsurance) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(insurance)
            session.transaction.commit()
        }
    }

    fun update(insurance: CarInsurance) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(insurance)
            session.transaction.commit()
        }
    }
}