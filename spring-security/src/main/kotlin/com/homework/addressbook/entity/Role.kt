package com.homework.addressbook.entity

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name="role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "name", nullable = false)
    var  name: String,
): GrantedAuthority
{
    override fun getAuthority(): String {
        return name;
    }
}