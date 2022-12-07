package ru.sber.orm.Dao

import org.hibernate.SessionFactory
import ru.sber.orm.Entities.Client

class ClientDao(sessionFactory: SessionFactory) : BaseDao<Client>(sessionFactory, Client::class.java) {
}