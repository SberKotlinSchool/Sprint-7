package ru.sber.orm.db.dao

import org.hibernate.SessionFactory
import ru.sber.orm.db.entity.Actor

class ActorDao(private val sessionFactory:SessionFactory) {

    fun save(entity: Actor) {
        sessionFactory.openSession().use { session ->
            session.apply {
                beginTransaction()
                save(entity)
                transaction.commit()
            }
        }
    }

    fun delete(entity: Actor) {
        sessionFactory.openSession().use { session ->
            session.apply {
                beginTransaction()
                delete(entity)
                transaction.commit()
            }
        }
    }

    fun getById(id: Long): Actor {
        var res: Actor
        sessionFactory.openSession().use {session->
            session.apply {
                beginTransaction()
                res = get(Actor::class.java, id)
                transaction.commit()
            }
        }
        return res
    }
}