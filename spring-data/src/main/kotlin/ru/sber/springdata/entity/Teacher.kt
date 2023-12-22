package ru.sber.orm.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Teacher(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var fullName: String = "",

    @ManyToMany(mappedBy = "teachers")
    var groups: List<Group> = mutableListOf()
)