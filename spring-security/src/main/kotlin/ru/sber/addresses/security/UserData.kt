package ru.sber.addresses.security

import javax.persistence.*

@Entity
data class UserData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val username: String,

    var password: String,

    var groups: String
) {
    fun toUserDetails() = UserDetailsAdapter(this)
}
