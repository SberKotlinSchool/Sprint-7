package ru.sber.orm.dao

import org.hibernate.SessionFactory

open class BaseDAO<T>(private val sessionFactory: SessionFactory) {
    fun create(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(entity)
            session.transaction.commit()
        }
    }
    fun getById(id: Long, entityType: Class<T>): T? {
        val result: T?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(entityType, id)
            session.transaction.commit()
        }
        return result
    }

    fun update(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(entity)
            session.transaction.commit()
        }
    }

    fun delete(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(entity)
            session.transaction.commit()
        }
    }
}