package com.homework.springdata.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
class CarPassport(

    @Id
    @GeneratedValue
    val passportId: Long = 0,

    @ManyToOne(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    val owner: Person,

    @OneToOne(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    val car: Car,
){
    override fun toString(): String {
        return "CarPassport(passportId=$passportId, owner=${owner.firstName}, car=${car.details})"
    }
}