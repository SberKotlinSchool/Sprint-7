package ru.sber.orm.Dao

import org.hibernate.SessionFactory
import ru.sber.orm.Entities.IssueReason

class IssueReasonDao(sessionFactory: SessionFactory) : BaseDao<IssueReason>(sessionFactory, IssueReason::class.java) {
}