package com.example.demo.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import org.springframework.data.annotation.Id

@Entity
data class Entity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    var ammount: Long?
)