package ru.sber.orm.Dao

import org.hibernate.SessionFactory

abstract class BaseDao(val sessionFactory: SessionFactory) {
    fun <T> create(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(entity)
            session.transaction.commit()
        }
    }

    fun <T> update(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(entity)
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

    fun <T> findById(id: Long, entity: Class<T>): T? {
        val result: T?

        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(entity, id)
            session.transaction.commit()
        }

        return result
    }

    fun <T> findAll(table: String): List<T>? {
        val result: List<T>?

        sessionFactory.openSession().use {session ->
            session.beginTransaction()
            result = session.createQuery("from $table").list() as? List<T>
            session.transaction.commit()
        }

        return result
    }
}