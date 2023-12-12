package ru.shadowsith.addressbook.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails


@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val login: String? = null,

    private var password: String? = null,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: List<Role> = emptyList()

) : UserDetails {
    override fun getUsername() = this.login!!

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    override fun getAuthorities() = this.roles

    override fun getPassword() = this.password

    fun setPassword(password: String?) {
        this.password = password
    }

    override fun toString(): String {
        return "User(id=$id, login=$login, password=$password, roles=$roles)"
    }
}