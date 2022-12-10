package ru.sber.addresses.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsAdapter(private val user: UserData) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        user.groups.split(",").map(::SimpleGrantedAuthority).toMutableList()


    override fun getPassword() = user.password

    override fun getUsername() = user.username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}