package com.example.adressbook.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "users")
class AddressUser(
    @Id
    @GeneratedValue
    val id: Long? = null,
    @Column(name = "user_name", unique = true)
    val userName: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: UserRole
)

enum class UserRole(
    val authorities: Set<UserAuthority>
) {
    ROLE_API(
        setOf(UserAuthority.API_ACCESS)
    ),
    ROLE_APP(
        setOf(UserAuthority.APP_ACCESS)
    ),
    ROLE_ADMIN(
        setOf(UserAuthority.APP_ACCESS, UserAuthority.API_ACCESS)
    );
}

enum class UserAuthority : GrantedAuthority {
    API_ACCESS, APP_ACCESS;

    override fun getAuthority(): String = name
}

