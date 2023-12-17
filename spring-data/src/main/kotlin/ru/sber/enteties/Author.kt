package ru.sber.enteties

import javax.persistence.*

@Entity
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    var firstName: String,

    var lastName: String,
) {
    override fun toString(): String {
        return "Author(firstName=$firstName, lastName=$lastName)"
    }
}