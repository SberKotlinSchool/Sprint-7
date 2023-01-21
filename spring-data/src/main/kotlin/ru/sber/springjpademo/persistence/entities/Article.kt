package ru.sber.springjpademo.persistence.entities

import javax.persistence.*

@Entity
@Table(name="������")
class Article (
    @Id
    @GeneratedValue
    var id: Long = 0,
    var science: String,
    var publicistic: String,
)