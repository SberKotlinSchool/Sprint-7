package ru.sber.enteties

import javax.persistence.*

@Entity
class Publish(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    var name: String,
) {
    override fun toString(): String {
        return "Publish(name=$name)"
    }
}