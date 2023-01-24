package ru.sber.springjpademo.persistence.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "homeaddress")
class HomeAddress(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var street: String,
)