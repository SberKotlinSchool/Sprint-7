package com.example.springdata.model

import jakarta.persistence.*

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    val id: Int? = null,
    val city: String? = null,

    val street: String? = null,

    val building: String? = null

) {
    override fun toString(): String {
        return "Address(id=$id, city=$city, street=$street, building=$building)"
    }
}
