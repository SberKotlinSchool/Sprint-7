package com.example.bookstore.entity;

import javax.persistence.*

@Entity
class Author (

    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "firstName", nullable = false)
    var firstName: String,

    @Column(name = "secondName")
    var secondName: String,

    @Column(name = "lastName")
    var lastName: String,

)
{
    override fun toString(): String {
        return "Author(id=$id, firstName='$firstName', secondName='$secondName', lastName=$lastName)"
    }
}
