package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Shop

class ShopDAO(private val sessionFactory: SessionFactory) {

  fun save(shop: Shop) {
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      session.save(shop)
      session.transaction.commit()
    }
  }

  fun find(id: Long): Shop? =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      val address = session.get(Shop::class.java, id)
      session.transaction.commit()
      address
    }

  fun findAll(): List<Shop> =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      val result = session.createQuery("from Shop").list() as List<Shop>
      session.transaction.commit()
      result
    }

  fun delete(shop: Shop) =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      session.delete(shop)
      session.transaction.commit()
    }

}