package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Address

class AddressDAO(private val sessionFactory: SessionFactory) {

  fun save(address: Address) {
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      session.save(address)
      session.transaction.commit()
    }
  }

  fun find(id: Long): Address? =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      val address = session.get(Address::class.java, id)
      session.transaction.commit()
      address
    }

  fun findAll(): List<Address> =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      val result = session.createQuery("from Address").list() as List<Address>
      session.transaction.commit()
      result
    }

  fun delete(address: Address) =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      session.delete(address)
      session.transaction.commit()
    }
}