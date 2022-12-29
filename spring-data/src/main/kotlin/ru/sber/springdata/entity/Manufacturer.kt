package ru.sber.springdata.entity

import javax.persistence.*

@Entity
class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    var name: String? = null
}