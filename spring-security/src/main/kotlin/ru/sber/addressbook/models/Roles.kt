package ru.sber.addressbook.models

import javax.persistence.*


@Entity
class Roles (
    @Id
    @GeneratedValue
    val id: Long = 0,
    var roles: String
) {
    @Override
    override fun toString(): String {
        return "role: $roles"
    }
}