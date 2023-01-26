package ru.sber.app.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "ADDRESSBOOK")
data class AddressBook(
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