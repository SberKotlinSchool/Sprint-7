package com.example.demo.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Address(
  @Id
  @GeneratedValue
  var id: Long? = null,
  var name: String = "",
  var email: String = ""
)