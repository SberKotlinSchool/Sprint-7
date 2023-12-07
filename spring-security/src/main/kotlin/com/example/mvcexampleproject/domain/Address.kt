package com.example.mvcexampleproject.domain

import javax.persistence.*

@Entity
@Table(name = "addresses")
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = null,
    var city: String? = null,
    var phone: String? = null
)
