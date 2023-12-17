package ru.sber.springdata.entity

import javax.persistence.*

@Entity
data class Author(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var firstName: String,
    var lastName: String,
    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    var books: MutableList<Book>? = null,
)