package ru.sber.springjpademo.persistence.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import ru.sber.springjpademo.persistence.entity.HomeAddress
import ru.sber.springjpademo.persistence.entity.PersonalData
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany

@Entity
class Student(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "first_name", length = 127)
    var name: String,

    @NaturalId
    var email: String,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "studytype")
    var studyType: StudyType,

    @Column(name = "birthdate")
    var birthDate: LocalDate,

    var personalData: PersonalData,

    @OneToMany(targetEntity = HomeAddress::class, cascade = [CascadeType.ALL])
    @JoinTable(name = "student_homeaddress",
        joinColumns = [JoinColumn(name = "student_id")],
        inverseJoinColumns = [JoinColumn(name = "homeaddress_id")]
    )
    var homeAddress: List<HomeAddress>,

    @CreationTimestamp
    @Column(name = "createdtime")
    var createdTime: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updatedtime")
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Student(id=$id, name='$name', email='$email', studyType=$studyType, birthDate=$birthDate)"
    }
}
