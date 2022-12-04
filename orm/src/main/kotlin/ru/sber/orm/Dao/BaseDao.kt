package ru.sber.orm.Dao

import org.hibernate.SessionFactory

abstract class BaseDao<T>(
    val sessionFactory: SessionFactory,
    private val entityType: Class<T>
) {

    fun create(entity: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(entity)
            session.transaction.commit()
        }
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

    open fun findById(id: Long): T? {
        val result: T?

        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(entityType, id)
            session.transaction.commit()
        }

        return result
    }

    fun findAll(table: String): List<T>? {
        val result: List<T>?

        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from $table", entityType).list() as List<T>
            session.transaction.commit()
        }

        return result
    }
}