package ru.sber.orm.entities

import javax.persistence.*

@Entity
class Pilot(
    @Id
    @GeneratedValue
    var id: Long? = 0,

    var name: String,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var plains: MutableList<Aircraft> = ArrayList()
)