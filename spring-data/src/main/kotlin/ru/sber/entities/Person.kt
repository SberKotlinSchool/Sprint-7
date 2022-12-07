package ru.sber.entities

import javax.persistence.*

@Entity
class Person (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name="name_", length=200, nullable = false)
    var name: String,

    @Enumerated(value = EnumType.STRING)
    var sex : Sex,


) {
    override fun toString(): String {
        return "Person(id=$id, name='$name', sex=$sex)"
    }
}

enum class Sex {
    M, W
}