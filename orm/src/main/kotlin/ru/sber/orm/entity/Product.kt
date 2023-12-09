package ru.sber.orm.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Product(
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  var id: Long = 0,

  @Column(name = "name", length = 127, nullable = false)
  var name: String = "",

  @Column(name = "price", nullable = false)
  var price: Double = 0.0,

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "products")
  var shops: MutableSet<Shop> = mutableSetOf()
)
