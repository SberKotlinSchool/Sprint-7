package ru.sber.astafex.springmvc.entity

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
    val id: Long,
    @Column(nullable = false, unique = true)
    val username: String,
    var password: String,
    var groups: String,
    var expired: Boolean,
    var locked: Boolean,
    var credExpired: Boolean,
    var enabled: Boolean
) {
    fun toUserDetails(): UserDetails = UserDetailsAdapter(this)
}

class UserDetailsAdapter(private val user: User) : UserDetails {
    private val authoritiesList: MutableCollection<out GrantedAuthority>

    init {
        authoritiesList = user.groups.split(",").stream().map(::SimpleGrantedAuthority).collect(Collectors.toList())
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList
    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.username
    override fun isAccountNonExpired(): Boolean = !user.expired
    override fun isAccountNonLocked(): Boolean = !user.locked
    override fun isCredentialsNonExpired(): Boolean = !user.credExpired
    override fun isEnabled(): Boolean = user.enabled
}
