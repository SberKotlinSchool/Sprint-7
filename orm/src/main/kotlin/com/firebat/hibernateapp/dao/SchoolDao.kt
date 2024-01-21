package com.firebat.hibernateapp.dao

import com.firebat.hibernateapp.entity.School
import org.hibernate.SessionFactory

class SchoolDao(private val sessionFactory: SessionFactory) {
    fun save(school: School) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(school)
            session.transaction.commit()
        }
    }

    fun findById(id: Long): School? {
        val result: School?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(School::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findByPositionInTheSchoolCompetitionUnknown(): List<School> {
        val result: List<School>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("FROM School WHERE positionInTheSchoolCompetition IS NULL")
            result = query.resultList as List<School>
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<School> {
        val result: List<School>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("FROM School").list() as List<School>
            session.transaction.commit()
        }
        return result
    }

    fun update(school: School) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(school)
            session.transaction.commit()
        }
    }

    fun delete(school: School) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(school)
            session.transaction.commit()
        }
    }
}