package com.homework.springdata.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class CarInsurance(

    @Id
    @GeneratedValue
    val insuranceId: Long = 0,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var drivers: List<Person>,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val car: Car
) {
    override fun toString(): String {
        return "CarInsurance(insuranceId=$insuranceId, drivers=${drivers.map { it.firstName }}, car=$car)"
    }
}