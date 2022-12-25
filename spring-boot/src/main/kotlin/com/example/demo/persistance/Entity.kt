package com.example.demo.persistance

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue


@Entity
data class Entity (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column
    var name: String = ""
)