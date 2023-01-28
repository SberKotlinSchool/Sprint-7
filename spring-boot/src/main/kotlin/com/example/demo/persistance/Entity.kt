package com.example.demo.persistance

import javax.persistence.*
import javax.persistence.Entity

@Entity
data class Entity (
    @Column(nullable = false)
    val name: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)