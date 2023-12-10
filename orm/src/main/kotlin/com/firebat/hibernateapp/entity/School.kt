package com.firebat.hibernateapp.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class School(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(nullable = false)
    var name: String,

    var positionInTheSchoolCompetition: Int? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "principal_id", nullable = false)
    var principal: Principal,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "School(id=$id, name='$name', positionInTheSchoolCompetition=$positionInTheSchoolCompetition, principal=$principal, createdTime=$createdTime, updatedTime=$updatedTime)"
    }
}