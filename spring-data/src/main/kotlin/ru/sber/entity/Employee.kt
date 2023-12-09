package ru.sber.entity

import org.hibernate.annotations.NaturalId
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "employee")
class Employee(
        @Id
        @GeneratedValue
        var id: Long = 0,

        @Column(name = "name", length = 128)
        var name: String?,

        var employmentDate: LocalDate?,

        @NaturalId // Дополнительный ключ на уровне hibernate
        var tableNum: String?,

        var grade: Int?,

        @Enumerated(value = EnumType.STRING)
        var status: EmployeeStatus = EmployeeStatus.ACTIVE,


        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "position_id", referencedColumnName = "id")
        var position: Position?
) {
    override fun toString(): String {
        return "Employee(id=$id, '$name')"
    }
}

enum class EmployeeStatus {
    ACTIVE,
    FIRED
}