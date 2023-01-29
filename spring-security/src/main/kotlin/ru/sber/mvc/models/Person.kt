package ru.sber.mvc.models

import ru.sber.mvc.security.PersonDetails
import javax.persistence.*

@Entity
@Table(name = "person")
class Person (
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "user_name")
    val username: String? = null,

    @Column(name = "password")
    val password: String? = null,

    @Column(name = "user_roles") val roles: String? = null
) {
    override fun toString(): String {
        return "Person: id = ${id}; name = {$username}; roles = {$roles}"
    }
    fun toPersonDetails() = PersonDetails(this)
}