package ru.sber.springdata.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Position(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,

    @JoinColumn(name = "department_id")
    @ManyToOne
    var department: Department,

    var minSalary: Double,

    var maxSalary: Double,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Position(id=$id, name='$name', department=$department)"
    }
}