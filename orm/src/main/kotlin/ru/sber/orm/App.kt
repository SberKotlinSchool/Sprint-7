package ru.sber.orm

import ru.sber.orm.dao.AddressDAO
import ru.sber.orm.dao.ProductDAO
import ru.sber.orm.dao.ShopDAO
import ru.sber.orm.entity.Address
import ru.sber.orm.entity.Product
import ru.sber.orm.entity.Shop

fun main() {

  buildSessionFactory().use { sessionFactory ->
    val addressDAO = AddressDAO(sessionFactory)
    val address = Address(
      city = "SPb",
      street = "Main",
      houseNumber = 1,
    )

    val productDAO = ProductDAO(sessionFactory)
    val product = Product(
      name = "product",
      price = 10.0
    )

    val shopDAO = ShopDAO(sessionFactory)
    val shop = Shop(name = "shop", address = address)

    addressDAO.save(address)
    productDAO.save(product)
    shop.addProduct(product)
    shopDAO.save(shop)

    val foundShop = shopDAO.find(shop.id)
    println("Найден магазин: $foundShop \n")
    println("Найден продукт: ${productDAO.find(product.id)} \n")
    shopDAO.delete(shop)
  }
}