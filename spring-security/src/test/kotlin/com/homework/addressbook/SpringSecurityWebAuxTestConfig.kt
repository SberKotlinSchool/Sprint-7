package com.homework.addressbook

import com.homework.addressbook.entity.Role
import com.homework.addressbook.entity.User
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@TestConfiguration
class SpringSecurityWebAuxTestConfig {

    @Bean
    @Primary
//    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
    fun userDetailsService(): UserDetailsService {
        var ADMIN_ROLE = Role(1, "ADMIN")
        var USER_ROLE = Role(2, "USER")
        var API_ROLE = Role(3, "API")


        var ADMIN = User(1, "ADMIN", "ADMIN".toCharArray(), ADMIN_ROLE)
        var USER = User(2, "USER", "USER".toCharArray(), USER_ROLE)
        var API = User(3, "API", "API".toCharArray(), API_ROLE)

        return InMemoryUserDetailsManager( listOf(
            ADMIN, USER, API)
        );
    }
}