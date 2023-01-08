package com.example.persistance

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
data class Entity (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column
    var name: String = ""
)