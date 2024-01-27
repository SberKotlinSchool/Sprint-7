package ru.sber.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "records")
data class Record(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @Column(name = "name", length = 127)
    var name: String = "",

    @Column(name = "lastName", length = 127)
    var lastName: String = "",

    @Column(name = "secondName")
    var secondName: String = "",

    @Column(name = "phoneNumber")
    var phoneNumber: String = "",

    @Column(name = "city", length = 127)
    var city: String = "",

    @Column(name = "street", length = 127)
    var street: String = "",

    @Column(name = "house_number")
    var houseNumber: Int = 0,

    @Column(name = "post_code")
    var postcode: Int = 0,

    var username: String = ""
)