package ru.sber.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Note(
    @Id
    @GeneratedValue
    var id: Long? = null,

    var name: String,
    var address: String,
    var phone: String,
)