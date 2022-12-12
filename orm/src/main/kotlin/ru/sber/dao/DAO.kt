package ru.sber.dao

import org.hibernate.SessionFactory
import javax.persistence.Entity

class DAO(private val sessionFactory: SessionFactory) {
    private fun isEntityClass(clazz: Class<Any>) = clazz.getAnnotation(Entity::class.java) != null


    fun save(entity: Any) {
        if (!isEntityClass(entity.javaClass)) return;

        sessionFactory.openSession().use {session ->
            session.beginTransaction()
            session.save(entity)
            session.transaction.commit()
        }
    }

    fun <E> findById(id: Long, clazz: Class<E>) : E? {
        if (!isEntityClass(clazz.javaClass)) return null;
        val result: E?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(clazz, id)
            session.transaction.commit()
        }
        return result
    }

    fun <E> findAll(clazz: Class<E>) : List<E> {
        if (!isEntityClass(clazz.javaClass)) return emptyList();
        val result: List<E>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from ${clazz.canonicalName}").list() as List<E>
            session.transaction.commit()
        }
        return result
    }

    fun update(entity: Any) {
        if (!isEntityClass(entity.javaClass)) return;
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(entity)
            session.transaction.commit()
        }
    }

    fun delete(entity: Any) {
        if (!isEntityClass(entity.javaClass)) return;
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(entity)
            session.transaction.commit()
        }
    }

}