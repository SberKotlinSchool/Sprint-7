package com.example.demo.persistance

import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "book")
data class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,

    @Column(name = "name")
    private var name: String? = null,

    @Column(name = "author")
    private val author: String? = null
)