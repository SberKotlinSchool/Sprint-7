package ru.sber.entity

import javax.persistence.*

@Entity
data class Teacher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @ManyToMany(mappedBy = "teachers")
    val courses: MutableList<Course> = mutableListOf()
)
