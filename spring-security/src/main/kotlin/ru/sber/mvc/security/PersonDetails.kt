package ru.sber.mvc.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.sber.mvc.models.Person

class PersonDetails(private val person: Person) : UserDetails  {

    override fun getAuthorities(): MutableList<out GrantedAuthority>? =
        person.roles?.split(",")?.map(::SimpleGrantedAuthority)?.toMutableList()

    override fun getPassword() = person.password

    override fun getUsername() = person.username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}