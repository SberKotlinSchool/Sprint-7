package ru.sber.springjpademo.persistence.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "student")
class Student(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "first_name", length = 127)
    var name: String,

    @NaturalId
    var email: String,

    @Enumerated(value = EnumType.STRING)
    var studyType: StudyType,
    var birthDate: LocalDate,
    var personalData: PersonalData,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var homeAddress: HomeAddress,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var inventory: MutableList<Inventory>,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var university: University,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var article: MutableList<Article>,
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Student(id=$id, name='$name', email='$email', studyType=$studyType, birthDate=$birthDate)"
    }
}