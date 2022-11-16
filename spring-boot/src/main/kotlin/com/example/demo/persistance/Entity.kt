package com.example.demo.persistance

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id

@javax.persistence.Entity
data class Entity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column
    var name: String?
)