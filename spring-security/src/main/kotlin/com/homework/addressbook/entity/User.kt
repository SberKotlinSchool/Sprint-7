package com.homework.addressbook.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name="users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "login", nullable = false)
    var  login: String,

    @Column(name = "password", nullable = false)
    var  password: CharArray,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name =  "role_id")
    var role: Role
): UserDetails
{
    override fun getUsername(): String = login
    override fun getPassword(): String = String(password)
    override fun isEnabled(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun getAuthorities(): Set<out GrantedAuthority> = mutableSetOf(role)
}