package ru.sber.springdata.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Employee (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "last_name", length = 30)
    var lastName: String,

    @Column(name = "first_name", length = 30)
    var firstName: String,

    @Column(name = "middle_name", length = 30)
    var middleName: String,

    var birthDate: LocalDate,

    var personalData: PersonalData,

    @NaturalId
    var email: String,

    var phoneNumber: String,

    @Enumerated(value = EnumType.STRING)
    var workingMode: WorkingMode? = null,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Employee(id=$id, name='$lastName $firstName $middleName', phoneNumber = '$phoneNumber', email='$email')"
    }
}

enum class WorkingMode {
    REMOTE_MODE,
    OFFICE_MODE
}