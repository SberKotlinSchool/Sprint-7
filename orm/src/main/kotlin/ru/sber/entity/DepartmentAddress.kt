package ru.sber.entity

import javax.persistence.*

@Entity
@Table(name = "department_address")
class DepartmentAddress(
        @Id
        @GeneratedValue
        var id: Long = 0,

        var fullAddress: String?,

        var postalCode: Int?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "department_id", referencedColumnName = "id")
        var department: Department

)