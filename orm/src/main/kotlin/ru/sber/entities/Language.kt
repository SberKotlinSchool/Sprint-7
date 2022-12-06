package ru.sber.entities

import org.hibernate.annotations.NaturalId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Language (
        @Id
        @GeneratedValue
        var id: Long = 0,

        @Column(name="name_", length=200, nullable = false)
        @NaturalId
        var name: String,
) {
        override fun toString(): String {
                return "Language(id=$id, name='$name')"
        }
}