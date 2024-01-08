package dao

import entity.ContactInformation
import entity.Magician
import org.hibernate.SessionFactory

class MagicianDao(private val sessionFactory: SessionFactory) {
    fun save(magician: Magician) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(magician)
            session.transaction.commit()
        }
    }

    fun findById(id: Int): Magician? {
        val res: Magician?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.get(Magician::class.java, id)
            session.transaction.commit()
        }
        return res
    }

    fun find(contactInformation: ContactInformation): Magician? {
        val res: Magician?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.byNaturalId(Magician::class.java)
                .using("contactInformation", contactInformation).loadOptional()
                .orElse(null)
            session.transaction.commit()
        }
        return res
    }

    fun findAll(): List<Magician> {
        val res: List<Magician>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.createQuery("from magician").list() as List<Magician>
            session.transaction.commit()
        }
        return res
    }

    fun update(magician: Magician): Magician {
        val res: Magician
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            res = session.merge(magician) as Magician
        }
        return res
    }

    fun delete(magician: Magician) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(magician)
            session.transaction.commit()
        }
    }
}