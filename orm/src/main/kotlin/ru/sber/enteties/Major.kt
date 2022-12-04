package ru.sber.enteties

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Major(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    val code: String,
    val name: String,
) {
    override fun toString(): String {
        return "Major: $code $name"
    }
}