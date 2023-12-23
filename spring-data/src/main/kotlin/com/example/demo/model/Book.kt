package com.example.demo.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.ManyToOne
import org.springframework.data.annotation.Id

@Entity
data class Book(
    @Id
    @GeneratedValue
    var id: Long? = null,

    var text: Long = 0,

    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST])
    var author: Author? = null
)