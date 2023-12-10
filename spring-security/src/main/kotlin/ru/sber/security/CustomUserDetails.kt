package ru.sber.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.sber.persistence.UserAuthorityEntity

class CustomUserDetails : UserDetails {
    private val authoritiesList: MutableCollection<out GrantedAuthority>
    private val userAuthorityEntity: UserAuthorityEntity

    constructor(userAuthorityEntity: UserAuthorityEntity) {
        authoritiesList = userAuthorityEntity.userAuthorityGroupList
                .map { SimpleGrantedAuthority(it.group.name) }
                .toMutableList()

        this.userAuthorityEntity = userAuthorityEntity
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList

    override fun getPassword(): String = userAuthorityEntity.password

    override fun getUsername(): String = userAuthorityEntity.username

    override fun isAccountNonExpired(): Boolean = !userAuthorityEntity.expired

    override fun isAccountNonLocked(): Boolean = !userAuthorityEntity.locked

    override fun isCredentialsNonExpired(): Boolean = !userAuthorityEntity.credExpired

    override fun isEnabled(): Boolean = userAuthorityEntity.enabled
}