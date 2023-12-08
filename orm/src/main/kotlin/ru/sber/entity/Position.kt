package ru.sber.entity

import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "position")
class Position(

        @Id
        @GeneratedValue
        var id: Long = 0,

        var name: String?,

        @UpdateTimestamp
        var updatedAt: LocalDateTime? = null,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "department_id", referencedColumnName = "id")
        var department: Department?,

        @OneToMany(mappedBy = "position", cascade = [CascadeType.ALL], targetEntity = Employee::class, orphanRemoval = true)
        var employeeList: MutableList<Employee> = mutableListOf()

) {
    override fun toString(): String {
        return "name(id=$id, '$name')"
    }
}