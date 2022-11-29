package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Drone

class DroneDAO(sessionFactory: SessionFactory) : BaseDAO<Drone>(sessionFactory) {
    fun getById(id: Long): Drone? {
        return getById(id, Drone::class.java)
    }
}