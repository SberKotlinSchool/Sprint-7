package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Product

class ProductDAO(private val sessionFactory: SessionFactory) {

  fun save(product: Product) {
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      session.save(product)
      session.transaction.commit()
    }
  }

  fun find(id: Long): Product? = sessionFactory.openSession().use { session ->
    session.beginTransaction()
    val address = session.get(Product::class.java, id)
    session.transaction.commit()
    address
  }

  fun findAll(): List<Product> =
    sessionFactory.openSession().use { session ->
      session.beginTransaction()
      val result = session.createQuery("from Product").list() as List<Product>
      session.transaction.commit()
      result
    }

  fun delete(product: Product) = sessionFactory.openSession().use { session ->
    session.beginTransaction()
    session.delete(product)
    session.transaction.commit()
  }

}