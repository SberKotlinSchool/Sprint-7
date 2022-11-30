package dao

import entity.LibraryCard
import org.hibernate.SessionFactory

class LibraryDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(libraryCard: LibraryCard) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(libraryCard)
            session.transaction.commit()
        }
    }

    fun find(id: Long): LibraryCard? {
        val result: LibraryCard?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(LibraryCard::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<LibraryCard> {
        val result: List<LibraryCard>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from library_card", LibraryCard::class.java).list() as List<LibraryCard>
            session.transaction.commit()
        }
        return result
    }

    fun delete(libraryCard: LibraryCard) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(libraryCard)
            session.transaction.commit()
        }
    }

    fun update(libraryCard: LibraryCard) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(libraryCard)
            session.transaction.commit()
        }
    }
}