package ru.sber.entity

import javax.persistence.*

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val street: String,

    @OneToOne(mappedBy = "address")
    val student: Student
)