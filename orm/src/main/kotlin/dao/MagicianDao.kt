package dao

import entity.Citizen
import org.hibernate.SessionFactory

class MagicianDao(private val sessionFactory: SessionFactory) {

    fun save(citizen: Citizen) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(citizen.address)
            citizen.contactInformation.forEach { session.save(it) }
            session.save(citizen)
            session.transaction.commit()
        }
    }

    fun findById(id: Int): Citizen? {
        val res: Citizen?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.get(Citizen::class.java, id)
            session.transaction.commit()
        }
        return res
    }

    fun find(firstName: String): Citizen? {
        val res: Citizen?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.byNaturalId(Citizen::class.java)
                .using("firstName", firstName).loadOptional()
                .orElse(null)
            session.transaction.commit()
        }
        return res
    }

    fun findAll(): List<Citizen> {
        val res: List<Citizen>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.createQuery("from Citizen").list() as List<Citizen>
            session.transaction.commit()
        }
        return res
    }

    fun update(citizen: Citizen): Citizen {
        val res: Citizen
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.merge(citizen) as Citizen
        }
        return res
    }

    fun delete(citizen: Citizen) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(citizen)
            session.transaction.commit()
        }
    }
}