package com.example.bookstore.entity;

import javax.persistence.*

@Entity
@Table(name="author")
class Author (

    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "firstname", nullable = false)
    var firstName: String,

    @Column(name = "secondname")
    var secondName: String,

    @Column(name = "lastname")
    var lastName: String,

    )
{
    override fun toString(): String {
        return "Author(id=$id, firstName='$firstName', secondName='$secondName', lastName=$lastName)"
    }
}
