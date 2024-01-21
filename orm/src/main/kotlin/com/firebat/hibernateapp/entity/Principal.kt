package com.firebat.hibernateapp.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Principal(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false)
    var yearsOfRuling: Int = 0,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Principal(id=$id, firstName='$firstName', lastName='$lastName', yearsOfRuling=$yearsOfRuling, createdTime=$createdTime, updatedTime=$updatedTime)"
    }
}