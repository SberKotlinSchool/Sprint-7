package com.example.demo.persistance

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Client(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column
    var lastName: String = "",
    @Column
    var firstName: String = "",
    @Column
    var middleName: String? = "",
    @Column
    var birthDate: LocalDate? = null,
    @Column
    var phoneNumber: String = "",
    @Column
    var email: String = ""
)
