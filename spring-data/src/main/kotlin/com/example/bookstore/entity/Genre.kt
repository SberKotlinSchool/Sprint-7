package com.example.bookstore.entity

import javax.persistence.*

@Entity
@Table(name="genre")
class Genre (

    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "name", nullable = false)
    var name: String
)