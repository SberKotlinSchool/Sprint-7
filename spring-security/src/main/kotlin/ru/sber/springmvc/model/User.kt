package ru.sber.springmvc.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import kotlin.streams.toList

@Entity
@Table(name = "user_data")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long,
    var name: String,
    @Column(nullable = false, unique = true)
    var login: String,
    var password: String?,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var address: Address? = null,
    var groups: String,
    var expired: Boolean,
    var locked: Boolean,
    var credExpired: Boolean,
    var enabled: Boolean
) {
    constructor() : this(0, "", "", "", null, "", false, false, false, true)

    override fun toString(): String {
        return "id = $id, name = $name, login = $login, password = $password, address = [${address?.addr}]"
    }

    fun toUserDetails(): UserDetails = UserDetailsAdapter(this)
}

class UserDetailsAdapter(val user: User) : UserDetails {

    private val authoritiesList: List<GrantedAuthority>

    init {
        authoritiesList = user.groups.split(",").stream().map(::SimpleGrantedAuthority).toList()
    }

    override fun getAuthorities(): List<GrantedAuthority> = authoritiesList

    override fun getPassword(): String? = user.password

    override fun getUsername(): String = user.login

    override fun isAccountNonExpired(): Boolean = !user.expired

    override fun isAccountNonLocked(): Boolean = !user.locked

    override fun isCredentialsNonExpired(): Boolean = !user.credExpired

    override fun isEnabled(): Boolean = user.enabled

}