package ru.sber.springdata.entity

import javax.persistence.*


@Entity
class Passport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    var person: Person?,
) {
    override fun toString(): String {
        return "Passport(id=$id, person=$person)"
    }
}