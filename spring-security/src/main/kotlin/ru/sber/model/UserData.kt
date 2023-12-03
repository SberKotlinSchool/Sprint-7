package ru.sber.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class UserData(
    @Id
    @GeneratedValue
    var id: Long? = null,

    val login: String,
    var password: String,
)