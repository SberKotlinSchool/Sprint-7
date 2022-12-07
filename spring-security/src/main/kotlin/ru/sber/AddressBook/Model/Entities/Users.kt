package ru.sber.AddressBook.Model.Entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Users")
class Users (
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(name = "user_name", unique = true)
    val userName: String,

    val password: String,

    @Column(name = "roles")
    val roles: String,

    @Column(name = "is_locked")
    var locked: Boolean,

    @Column(name = "is_expired")
    var expired: Boolean,

    @Column(name = "is_cred_Expired")
    var credExpired: Boolean,

    @Column(name = "enabled")
    var enabled: Boolean
) {
    fun toUserDetails(): UserDetails = UserDetailsAdapter(this)
}

class UserDetailsAdapter(private val user: Users) : UserDetails {

    private val authoritiesList: MutableCollection<out GrantedAuthority>

    init {
        authoritiesList = user.roles.split(",").stream().map(::SimpleGrantedAuthority).toList()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.userName

    override fun isAccountNonExpired(): Boolean = !user.expired

    override fun isAccountNonLocked(): Boolean = !user.locked

    override fun isCredentialsNonExpired(): Boolean = !user.credExpired

    override fun isEnabled(): Boolean = user.enabled

}

