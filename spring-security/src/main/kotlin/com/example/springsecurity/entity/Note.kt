package com.example.springsecurity.entity

import javax.persistence.*

@Entity
@Table(name = "notes")
data class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    var text: String = "",

    var author: String = ""
) {
    override fun toString(): String {
        return text
    }
}