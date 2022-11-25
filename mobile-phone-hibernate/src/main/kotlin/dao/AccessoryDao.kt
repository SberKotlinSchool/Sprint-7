package dao

import entity.Accessory
import entity.MobilePhone
import org.hibernate.SessionFactory

class AccessoryDao(val sessionFactory: SessionFactory) {

    fun save(accessory: Accessory): Long {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val phoneId = session.save(accessory) as Long
            session.transaction.commit()
            return phoneId
        }
    }

    fun update(accessory: Accessory) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(accessory)
            session.transaction.commit()
        }
    }

    fun findById(id: Long): Accessory? {
        val result: Accessory?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Accessory::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun delete(accessory: Accessory) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(accessory)
            session.transaction.commit()
        }
    }
}