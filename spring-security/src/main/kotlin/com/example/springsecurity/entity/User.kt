package com.example.springsecurity.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    var login: String = "",

    var pass: String = "",

    var roles: String = "",

    var expired: Boolean = false,

    var locked: Boolean = false,

    var credExpired: Boolean = false,

    var enabled: Boolean = true
) {
    fun toUserDetails(): UserDetails = UserDetailsAdapter(this)
}

class UserDetailsAdapter(val user: User) : UserDetails {

    private val authoritiesList: MutableCollection<out GrantedAuthority>

    init {
        authoritiesList = user.roles.split(",").stream().map(::SimpleGrantedAuthority).collect(Collectors.toList())
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList

    override fun getPassword(): String = user.pass

    override fun getUsername(): String = user.login

    override fun isAccountNonExpired(): Boolean = !user.expired

    override fun isAccountNonLocked(): Boolean = !user.locked

    override fun isCredentialsNonExpired(): Boolean = !user.credExpired

    override fun isEnabled(): Boolean = user.enabled

}