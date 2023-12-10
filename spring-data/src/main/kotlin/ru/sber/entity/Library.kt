package ru.sber.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class Library(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var name: String,
    @ManyToMany(mappedBy = "libraries")
    var books: MutableList<Book>? = null
) {
    override fun toString() = "'$name'"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Library

        return name == other.name
    }

    override fun hashCode() = name.hashCode()

}