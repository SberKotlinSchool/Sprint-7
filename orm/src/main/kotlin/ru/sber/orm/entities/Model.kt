package ru.sber.orm.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Model (
    @Id
    @GeneratedValue
    var id: Long? = 0,

    var name: String
)