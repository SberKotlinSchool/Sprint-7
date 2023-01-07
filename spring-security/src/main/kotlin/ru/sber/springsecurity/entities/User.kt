package ru.sber.springsecurity.entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import kotlin.streams.toList

@Entity
@Table(name = "Users")
data class User(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    val name: String = "",
    val password: String = "",
    val roles: String = "",
    var locked: Boolean = false,
    var expired: Boolean = false,
    var credExpired: Boolean = false,
    var enabled: Boolean = true
) {
    fun toUserDetails(): UserDetails = UserDetailsAdapter(this)
}

class UserDetailsAdapter(private val user: User) : UserDetails {

    private val authoritiesList: List<GrantedAuthority>

    init {
        authoritiesList = user.roles.split(",").stream().map(::SimpleGrantedAuthority).toList()
    }

    override fun getAuthorities(): List<GrantedAuthority> = authoritiesList

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.name

    override fun isAccountNonExpired(): Boolean = !user.expired

    override fun isAccountNonLocked(): Boolean = !user.locked

    override fun isCredentialsNonExpired(): Boolean = !user.credExpired

    override fun isEnabled(): Boolean = user.enabled

}