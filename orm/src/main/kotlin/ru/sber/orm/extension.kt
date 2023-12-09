package ru.sber.orm

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import ru.sber.orm.entity.Address
import ru.sber.orm.entity.Product
import ru.sber.orm.entity.Shop


fun buildSessionFactory(): SessionFactory = Configuration().configure()
  .addAnnotatedClass(Shop::class.java)
  .addAnnotatedClass(Address::class.java)
  .addAnnotatedClass(Product::class.java)
  .buildSessionFactory()