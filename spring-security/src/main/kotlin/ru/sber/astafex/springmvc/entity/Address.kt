package ru.sber.astafex.springmvc.entity

import javax.persistence.*

@Entity
@Table(name = "addresses")
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var name: String,
    var city: String,
    var phone: String
)