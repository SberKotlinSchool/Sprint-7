package enteties

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
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
    var studyType: StudyType,

    var birthDate: LocalDate,
    var personalData: PersonalData,
    @OneToMany
    var homeAddress: HomeAddress,
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Student(id=$id, name='$name', email='$email', studyType=$studyType, birthDate=$birthDate)"
    }
}

enum class StudyType {
    PART_TIME,
    FULL_TIME
}