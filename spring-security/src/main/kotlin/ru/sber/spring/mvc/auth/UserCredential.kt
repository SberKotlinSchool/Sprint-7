package ru.sber.spring.mvc.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
class UserCredential(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val username: String,

    var password: String,

    var groups: String
) {
    fun toUserDetails(): UserDetails {
        return UserDetailsAdapter(this)
    }
}

class UserDetailsAdapter(private val user: UserCredential) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return user.groups.split(",").map(::SimpleGrantedAuthority).toMutableList()
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

}