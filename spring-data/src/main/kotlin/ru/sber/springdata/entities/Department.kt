package ru.sber.springdata.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Department(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,

    @JoinColumn(name = "chief_id")
    @OneToOne
    var chief: Employee,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Department(id=$id, name='$name')"
    }
}