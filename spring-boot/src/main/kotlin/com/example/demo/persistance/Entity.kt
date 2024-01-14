package com.example.demo.persistance

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Entity

@Entity
data class Entity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    var ammount: Long?
)