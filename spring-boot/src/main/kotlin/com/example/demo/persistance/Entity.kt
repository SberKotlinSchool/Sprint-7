package com.example.demo.persistance

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue

@Entity
data class Entity(
    @javax.persistence.Id
    @GeneratedValue
    var id: Long? = null,
    @Column
    var name: String?
)
