package ru.sber.orm.Dao

import org.hibernate.SessionFactory
import ru.sber.orm.Entities.Account

class AccountDao(sessionFactory: SessionFactory) : BaseDao<Account>(sessionFactory, Account::class.java) {
    fun find(id: Long): Account? {
        val account: Account?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            account = session.get(Account::class.java, id)
            session.transaction.commit()
        }
        return account
    }

    fun find(externalId: String): Account? {
        val account: Account?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            account = session.byNaturalId(Account::class.java).using("externalId", externalId).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return account
    }
}