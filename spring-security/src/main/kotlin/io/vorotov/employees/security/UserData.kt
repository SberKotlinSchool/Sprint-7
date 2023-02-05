package io.vorotov.employees.security

import javax.persistence.*

@Entity
data class UserData(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val username: String,

    var password: String,

    var roles: String
) {
    fun toUserDetails() = UserDetailsAdapter(this)
}
