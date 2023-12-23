package com.example.demo.model

import jakarta.persistence.CascadeType
import jakarta.persistence.OneToMany
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import org.springframework.data.annotation.Id

@Entity
data class Author (
    @Id
    @GeneratedValue
    var id: Long? = null,

    var text: Long = 0,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    var books: List<Book>? = null
)