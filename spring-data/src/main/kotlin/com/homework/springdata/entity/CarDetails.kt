package com.homework.springdata.entity

import javax.persistence.Embeddable

@Embeddable
class CarDetails(
    val color: String = "",
    val brand: String = "",
){
    override fun toString(): String {
        return "CarDetails(color='$color', brand='$brand')"
    }
}

