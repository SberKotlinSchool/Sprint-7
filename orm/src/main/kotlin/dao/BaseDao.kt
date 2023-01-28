package dao

import org.hibernate.SessionFactory

interface BaseDao<T> {
    val sessionFactory: SessionFactory

    fun save(t: T) : Long {
        val id: Long
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            id = session.save(t) as Long
            session.transaction.commit()
        }
        return id
    }

    fun findById(id: Long, entityType: Class<T>): T? {
        val t: T?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            t = session.get(entityType, id)
            session.transaction.commit()
        }
        return t
    }

    fun findAll(simpleClassName: String): List<T> {
        val list: List<T>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            list = session.createQuery("from $simpleClassName").list() as List<T>
            session.transaction.commit()
        }
        return list
    }

    fun delete(t: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(t)
            session.transaction.commit()
        }
    }

    fun update(t: T) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(t)
            session.transaction.commit()
        }
    }
}
