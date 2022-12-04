package ru.sber.orm.models

import lombok.Data
import javax.persistence.*


@Entity
@Data
class Author(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var secondName: String,

    @Column(nullable = false)
    var lastName: String,
) {
    override fun toString(): String {
        return "Author(id=$id, lastName='$lastName', firstName='$firstName', secondName=$secondName)"
    }
}

