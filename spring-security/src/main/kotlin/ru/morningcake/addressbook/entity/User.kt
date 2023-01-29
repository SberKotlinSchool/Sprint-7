package ru.morningcake.addressbook.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.morningcake.addressbook.constant.Role
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "sec_user", schema = "sec")
class User (
    // custom props
    @Id
    @GeneratedValue
    var id: UUID? = null,

    @Column(name = "created_date", nullable = false, updatable = false)
    val createdDate: LocalDateTime? = LocalDateTime.now(),

    /** Имя и фамилия  */
    @Column(name = "name", nullable = false)
    var name: String? = null,

    /** Логин - уникальное значение  */
    @Column(name = "login", nullable = false, unique = true, updatable = false)
    val login: String,

    /** Пароль (от контроллера) или хэш пароля (на БД)  */
    @Column(name = "password_hash", nullable = false)
    var hash: String,

    /** Признак бана аккаунта  */
    @Column(name = "is_non_locked", nullable = false)
    var isNonLocked: Boolean = true,

    /** Роли  */
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", schema = "sec",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    @Column(name = "role", nullable = false)
    var roles: MutableSet<out GrantedAuthority> = mutableSetOf(Role.USER),

) : UserDetails {
    constructor() : this(null, null, null, "", "", true, mutableSetOf<Role>())

    override fun getUsername(): String = login
    override fun getPassword(): String = hash
    override fun isEnabled(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = isNonLocked
    override fun getAuthorities(): Set<out GrantedAuthority> = roles

    fun isAdmin() : Boolean {
        return roles.contains(Role.ADMIN)
    }
}