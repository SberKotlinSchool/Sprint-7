package com.example.demo.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.ManyToOne
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