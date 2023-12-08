package ru.sber.dao

import org.hibernate.SessionFactory

open class CommonRepository(
        private val sessionFactory: SessionFactory
) {

    fun <T> save(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(entity)
            session.transaction.commit()
        }
    }

    fun <T> delete(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(entity)
            session.transaction.commit()
        }
    }

    fun <T> find(id: Long, clazz: Class<T>): T? {
        val result: T?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(clazz, id)
            session.transaction.commit()
        }
        return result
    }
}