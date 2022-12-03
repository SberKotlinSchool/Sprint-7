package ru.sber.orm.Dao

import org.hibernate.SessionFactory

class ClientDao(sessionFactory: SessionFactory): BaseDao(sessionFactory) {
}