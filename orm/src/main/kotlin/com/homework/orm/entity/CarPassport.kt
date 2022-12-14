package com.homework.orm.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class CarOwner(

    @Id
    @GeneratedValue
    val id: Int,

    @Column
    val name: String
)