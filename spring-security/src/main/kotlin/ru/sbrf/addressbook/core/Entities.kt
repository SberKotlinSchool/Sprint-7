package ru.sbrf.addressbook.core

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import kotlin.jvm.Transient

@Entity
@Table(name = "EMPLOYEE")
data class Employee(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @field:NotBlank
    var firstName: String? = null,

    @field:NotBlank
    var lastName: String? = null,

    var owner: Long? = null,

    var patronymic: String? = null,

    var address: String? = null,

    var phone: String? = null,

    @field:Email
    val email: String? = null,

)

@Entity
@Table(name = "USER")
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

class UserDetailsAdapter(val user: User) : UserDetails {

    private val authoritiesList: MutableCollection<out GrantedAuthority>

    init {
        authoritiesList = user.groups.split(",")
            .stream()
            .map(::SimpleGrantedAuthority).toList()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = !user.expired

    override fun isAccountNonLocked(): Boolean = !user.locked

    override fun isCredentialsNonExpired(): Boolean = !user.credExpired

    override fun isEnabled(): Boolean = user.enabled

}

