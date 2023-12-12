package ru.shadowsith.addressbook.dto

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "roles")
data class Role(
    @Id
    val id: Long? = null,
    val name: String? = null,

): GrantedAuthority {
    override fun getAuthority() = name!!

    override fun toString(): String {
        return "Role(id=$id, name=$name)"
    }
}
