package ru.sber.orm.entities

import javax.persistence.*

@Entity
class Author(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var name: String,

    @OneToMany
    var book: Book,
)



