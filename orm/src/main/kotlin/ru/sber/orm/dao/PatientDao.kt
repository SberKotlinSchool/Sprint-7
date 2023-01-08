package ru.sber.orm.dao

import ru.sber.orm.entity.Patient
import org.hibernate.SessionFactory

class PatientDao (
    private val sessionFactory: SessionFactory
) {
    fun save(patient: Patient) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(patient)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Patient? {
        var result: Patient?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Patient::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun update(patient: Patient) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(patient)
            session.transaction.commit()
        }
    }
}