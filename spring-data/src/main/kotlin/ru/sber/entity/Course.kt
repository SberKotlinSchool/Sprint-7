package ru.sber.entity

import javax.persistence.*

@Entity
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true)
    val students: MutableList<Student> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "course_teacher",
        joinColumns = [JoinColumn(name = "course_id")],
        inverseJoinColumns = [JoinColumn(name = "teacher_id")]
    )
    val teachers: MutableList<Teacher> = mutableListOf()
)
