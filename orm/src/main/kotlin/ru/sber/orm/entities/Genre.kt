package ru.sber.orm.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "genre")
class Genre(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "genre_name")
    var name: String,

    )