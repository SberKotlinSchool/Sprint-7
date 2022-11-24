package com.example.demo.persistance

import javax.persistence.*

@javax.persistence.Entity
data class Entity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column
    var name: String
)
