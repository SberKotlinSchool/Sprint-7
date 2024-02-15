package ru.sber.addressbook.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class UserRole (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    val name: String,
)