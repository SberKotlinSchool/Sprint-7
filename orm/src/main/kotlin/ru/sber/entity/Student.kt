package ru.sber.entity

import javax.persistence.*

@Entity
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    val course: Course,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id", unique = true)
    val address: Address
)