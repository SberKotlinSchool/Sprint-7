package com.example.springdata.model

import jakarta.persistence.*

@Entity
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    val id: Int? = null,

    val age: Int? = null,

    val name: String? = null,

    @OneToOne(optional = false, cascade = [ CascadeType.ALL ])
    val passport: Passport? = null,

    @ManyToOne(optional = false, cascade = [ CascadeType.ALL ])
    @JoinColumn (name = "person_id")
    val address: Address? = null
) {
    override fun toString(): String {
        return "Person(id=$id, age=$age, name=$name, passport=$passport, address=$address)"
    }
}
