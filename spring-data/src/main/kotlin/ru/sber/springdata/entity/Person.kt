package ru.sber.springdata.entity

import javax.persistence.*
import javax.persistence.CascadeType.ALL

@Entity
@Table(name = "persons")
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var surname: String,
    var firstname: String,

    @OneToOne(mappedBy = "person", cascade = [ALL], orphanRemoval = true)
    var passport: Passport?
) {

    override fun toString(): String {
        return "Person (id=$id, surname=$surname, firstname=$firstname, passport (id=${passport?.id}))"
    }
}