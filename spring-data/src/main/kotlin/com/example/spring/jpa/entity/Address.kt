package com.example.spring.jpa.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Address(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var street: String,
) {
    override fun toString(): String {
        return "Address: $street"
    }
}