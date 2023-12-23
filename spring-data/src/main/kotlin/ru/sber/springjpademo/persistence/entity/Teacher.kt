package ru.sber.springjpademo.persistence.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "teacher")
class Teacher(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,

    var age: Int
)