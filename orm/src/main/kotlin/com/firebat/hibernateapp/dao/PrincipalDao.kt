package com.firebat.hibernateapp.dao

import com.firebat.hibernateapp.entity.Principal
import org.hibernate.SessionFactory

class PrincipalDao(private val sessionFactory: SessionFactory) {
    fun save(principal: Principal) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(principal)
            session.transaction.commit()
        }
    }

    fun findById(id: Long): Principal? {
        val result: Principal?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Principal::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findByYearsOfRulingMoreThen(yearsOfRuling: Int): List<Principal> {
        val result: List<Principal>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val query = session.createQuery("FROM Principal WHERE yearsOfRuling > :yearsOfRuling")
            query.setParameter("yearsOfRuling", yearsOfRuling)
            result = query.resultList as List<Principal>
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Principal> {
        val result: List<Principal>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("FROM Principal").list() as List<Principal>
            session.transaction.commit()
        }
        return result
    }

    fun update(principal: Principal) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(principal)
            session.transaction.commit()
        }
    }

    fun delete(principal: Principal) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(principal)
            session.transaction.commit()
        }
    }
}