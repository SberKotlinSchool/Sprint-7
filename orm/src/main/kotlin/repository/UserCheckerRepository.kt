package repository

import entity.UserChecker
import org.hibernate.SessionFactory

class UserCheckerRepository(
    private val sessionFactory: SessionFactory
) {

    fun save(userChecker: UserChecker) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(userChecker)
            session.transaction.commit()
        }
    }

    fun find(id: Long): UserChecker? {
        val result: UserChecker?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(UserChecker::class.java, id)
            session.transaction.commit()
        }
        return result
    }


    fun findAll(): List<UserChecker> {
        val result: List<UserChecker>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from UserChecker").list() as List<UserChecker>
            session.transaction.commit()
        }
        return result
    }
}