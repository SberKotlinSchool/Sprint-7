package ru.sber.enteties

import javax.persistence.*

@Entity
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Column(name = "first_name")
    var firstName: String,

    @Column(name = "last_name")
    var lastName: String,
) {
    override fun toString(): String {
        return "Author(firstName=$firstName, lastName=$lastName)"
    }
}