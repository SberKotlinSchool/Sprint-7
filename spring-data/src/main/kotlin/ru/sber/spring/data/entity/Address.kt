package ru.sber.spring.data.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Address(
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  var id: Long = 0,

  @Column(name = "city", length = 127, nullable = false)
  var city: String = "",

  @Column(name = "street", length = 127, nullable = false)
  var street: String = "",

  @Column(name = "house_number", nullable = false)
  var houseNumber: Int = 0,

  @OneToOne(fetch = FetchType.EAGER, mappedBy = "address")
  var shop: Shop? = null
)