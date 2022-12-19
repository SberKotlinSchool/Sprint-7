package dao

import enteties.Cat
import org.hibernate.SessionFactory

class CatsDao(
    private val sessionFactory: SessionFactory
) {
    fun save(cat: Cat) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(cat)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Cat? {
        val result: Cat?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Cat::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(name: String): Cat? {
        val result: Cat?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result =
                session.byNaturalId(Cat::class.java).using("name", name).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Cat> {
        val result: List<Cat>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Cat", Cat::class.java)
                .list() as List<Cat>
            session.transaction.commit()
        }
        return result
    }
}
