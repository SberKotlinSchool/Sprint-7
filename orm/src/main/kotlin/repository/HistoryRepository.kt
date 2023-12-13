package repository

import entity.History
import org.hibernate.SessionFactory

class HistoryRepository(
    private val sessionFactory: SessionFactory
) {

    fun save(history: History) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(history)
            session.transaction.commit()
        }
    }

    fun find(number: Long): History? {
        val result: History?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(History::class.java, number)
            session.transaction.commit()
        }
        return result
    }


    fun findAll(): List<History> {
        val result: List<History>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from History").list() as List<History>
            session.transaction.commit()
        }
        return result
    }
}