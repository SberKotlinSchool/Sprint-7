package com.example.demo.persistance


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "STUDENT")
data class Student(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(name = "first_name")
    var name: String?,

    @Column()
    var group: String?
)