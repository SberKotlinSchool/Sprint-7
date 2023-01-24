package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entities.Position

class PositionDAO(private val sessionFactory: SessionFactory
) {
    fun save(department: Position) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(department)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Position? {
        val result: Position?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Position::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Position> {
        val result: List<Position>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Position").list() as List<Position>
            session.transaction.commit()
        }
        return result
    }
}