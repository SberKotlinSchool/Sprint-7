package ru.sber.orm.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Product(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    val name: String,
    var weight: Long = 0
)