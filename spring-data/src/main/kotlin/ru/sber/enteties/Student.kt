package ru.sber.enteties

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import ru.sber.enteties.Major
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class Student(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @Column(name = "last_name", length = 127)
    var lastName: String,
    @Column(name = "first_name", length = 127)
    var firstName: String,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var address: Address,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var major: MutableList<Major>,
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Student (id=$id, lastName='$lastName', firstName='$firstName', address=$address, major=$major)"
    }
}