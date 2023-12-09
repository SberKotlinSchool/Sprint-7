package org.example.orm.model

import javax.persistence.*

@Entity
data class Passport(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    val id: Int? = null,

    val series: String? = null,

    val number: String? = null,

) {
    override fun toString(): String {
        return "Passport(id=$id, series=$series, number=$number)"
    }
}
