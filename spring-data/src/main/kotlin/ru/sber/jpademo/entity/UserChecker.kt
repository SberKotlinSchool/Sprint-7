package ru.sber.jpademo.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
class UserChecker(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "first_name", length = 127)
    var name: String,

    @NaturalId
    var email: String,

    ) {
    override fun toString(): String {
        return "User(id=$id, name='${name}', email='${email}"
    }
}
