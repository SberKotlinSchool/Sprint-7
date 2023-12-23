package com.example.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import org.springframework.data.annotation.Id

@Entity
data class Entity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    var ammount: Long?
)