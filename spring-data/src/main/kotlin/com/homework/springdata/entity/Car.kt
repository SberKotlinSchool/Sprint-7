package com.homework.springdata.entity

import javax.persistence.CascadeType
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Car(

    @Id
    @GeneratedValue
    val carId: Long = 0,

    @Embedded
    val details: CarDetails,

    @ManyToOne(cascade = [CascadeType.ALL])
    var passport: CarPassport? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    var insurance: CarInsurance? = null
){
    override fun toString(): String {
        return "Car(carId=$carId, details=$details, passport=${passport?.passportId}, insurance=${insurance?.insuranceId})"
    }
}