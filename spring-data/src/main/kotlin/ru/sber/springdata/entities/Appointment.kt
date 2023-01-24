package ru.sber.springdata.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import ru.sber.springdata.entities.Employee
import ru.sber.springdata.entities.Position
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Appointment(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @JoinColumn(name = "employee_id")
    @ManyToOne
    var employee: Employee,

    @JoinColumn(name = "post_id")
    @ManyToOne
    var post: Position,

    var salary: Double,

    var dateBegin: LocalDate,

    var dateEnd: LocalDate? = null,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
){
    override fun toString(): String {
        return "Appointment(id=$id, employee=$employee, post=$post, salary=$salary, dateBegin=$dateBegin, dateEnd=$dateEnd)"
    }
}