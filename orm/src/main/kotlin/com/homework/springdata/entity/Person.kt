package com.homework.springdata.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Person(
    @Id
    @GeneratedValue
    val personId: Long = 0,
    @Column
    val firstName: String,
    @Column
    val lastName: String,
    @Column
    val phoneNumber: String,
){
    override fun toString(): String {
        return "Person(id=$personId, firstName='$firstName', lastName='$lastName', phoneNumber='$phoneNumber')"
    }
}