package ru.sber.ufs.cc.kulinich.springmvc.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class AddressBookModel (
    @Id
    @GeneratedValue
    var id : Int = 0,


    val name : String,
    val phone : String
)