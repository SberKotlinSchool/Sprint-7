package ru.sber.study.kotlin.orm.persistence.entities

import javax.persistence.*

@Entity(name = "item_identity")
data class ItemIdentity(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne
    var developer: Developer,

    @Column(name = "catalognumber")
    var catalogNumber: String
)