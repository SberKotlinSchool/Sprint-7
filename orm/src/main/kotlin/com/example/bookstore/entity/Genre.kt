package com.example.bookstore.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Genre (

    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "name", nullable = false)
    var name: String
)