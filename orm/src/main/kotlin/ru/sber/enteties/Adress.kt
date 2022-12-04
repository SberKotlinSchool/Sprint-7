package ru.sber.enteties

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Address(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var city: String,
    var street: String,
) {
    override fun toString(): String {
        return "Address: $city $street"
    }
}