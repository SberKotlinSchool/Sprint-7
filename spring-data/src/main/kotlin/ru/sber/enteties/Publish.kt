package ru.sber.enteties

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Publish(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,

    var name: String,
) {
    override fun toString(): String {
        return "Publish(name=$name)"
    }
}