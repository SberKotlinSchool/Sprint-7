package ru.sber.app.entity

import javax.persistence.*


@Entity
//@Table(name = "ADDRESSBOOK")
data class ADDRESSBOOK(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "FIRSTNAME")
    var firstName: String,
    @Column(name = "LASTNAME")
    var lastName: String,

    var city: String,

    val owner: Long
) {
    fun toListString(): String = "$firstName $lastName $city"
}