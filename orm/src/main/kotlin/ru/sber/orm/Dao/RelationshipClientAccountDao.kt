package ru.sber.orm.Dao

import org.hibernate.SessionFactory
import ru.sber.orm.Entities.RelationshipClientAccount

class RelationshipClientAccountDao(sessionFactory: SessionFactory) :
    BaseDao<RelationshipClientAccount>(sessionFactory, RelationshipClientAccount::class.java) {

    override fun findById(accountId: Long): RelationshipClientAccount? {
        val result: RelationshipClientAccount?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(RelationshipClientAccount::class.java, accountId)
            session.transaction.commit()
        }
        return result
    }
}