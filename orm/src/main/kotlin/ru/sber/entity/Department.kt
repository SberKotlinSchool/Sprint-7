package ru.sber.entity

import javax.persistence.*

@Entity
@Table(name = "department")
class Department(
        @Id
        @GeneratedValue
        var id: Long = 0,

        var fullName: String?,

        var businessArea: String?,

        @OneToMany(mappedBy = "department", cascade = [CascadeType.ALL], targetEntity = Position::class, orphanRemoval = true)
        var positionList: MutableList<Position> = mutableListOf(),

        @OneToOne(mappedBy = "department", cascade = [CascadeType.ALL])
        var departmentAddress: DepartmentAddress? = null

) {

    override fun toString(): String {
        return "Department(id=$id, '$fullName')"
    }
}
