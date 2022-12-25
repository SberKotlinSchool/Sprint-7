package com.example.demo.entity

import javax.persistence.*

@Entity
class Patient(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var outpatientBook: OutpatientBook,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var hospitals: MutableList<Hospital>,

) {
    override fun toString(): String {
        return "Patient(id=$id, lastName='$lastName', firstName='$firstName', outpatientBook=$outpatientBook', hospital=$hospitals)"
    }
}