package ru.sber.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue
    val id: Long = 0,
    var roles: String,
) {
    @Override
    override fun toString(): String {
        return "role: $roles"
    }
}