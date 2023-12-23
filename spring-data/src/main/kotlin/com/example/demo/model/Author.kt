package com.example.demo.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.OneToMany
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