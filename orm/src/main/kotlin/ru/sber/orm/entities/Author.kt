package ru.sber.orm.entities

import javax.persistence.*

@Entity
@Table(name="author")
class Author(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var name: String,
)



