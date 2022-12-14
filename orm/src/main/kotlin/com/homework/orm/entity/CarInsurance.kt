package com.homework.orm.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class CarEnsurance(

    @Id
    @GeneratedValue
    val ensuranceId: Int = 0,

    @Column
    @OneToMany
    var drivers: List<Person>,

    @Column
    @OneToOne
    val car: Car
)