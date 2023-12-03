package com.example.demo.persistance

import javax.persistence.*


@javax.persistence.Entity
@Table(name = "entity")
data class Entity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,

        @Column
        var name: String?
) {
        constructor(name: String) : this(name = name, id = null)
}