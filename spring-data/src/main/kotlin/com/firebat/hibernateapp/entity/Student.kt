package com.firebat.hibernateapp.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "student2")
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Enumerated(EnumType.STRING)
    var successRate: SuccessRate = SuccessRate.UNDEFINED,

    @ManyToOne(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinColumn
    var school: School,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Student(id=$id, firstName='$firstName', lastName='$lastName', successRate=$successRate, school=$school, createdTime=$createdTime, updatedTime=$updatedTime)"
    }
}

enum class SuccessRate {
    GOLD, SILVER, UNDEFINED
}