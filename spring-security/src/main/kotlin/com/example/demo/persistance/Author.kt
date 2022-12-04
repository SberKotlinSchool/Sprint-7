package com.example.demo.persistance

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    var firstName: String? = "",

    var secondName: String? = "",

    val owner: Long
) {
    fun toListString(): String = "$secondName ($firstName)"
}

