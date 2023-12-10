package com.example.mvcexampleproject.configuration

import com.example.mvcexampleproject.services.CustomUserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder


@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http {
            authorizeRequests {
                authorize("/rest/**", "hasRole('ADMIN') or hasRole('API')")
                authorize("/app/**", "hasRole('ADMIN') or hasRole('USER')")
                authorize("/**", authenticated)
            }

            formLogin { permitAll() }

            csrf { disable() }

            headers { frameOptions { sameOrigin = true } }
        }
    }

    @Bean
    fun delegatingPasswordEncoder(): DelegatingPasswordEncoder = DelegatingPasswordEncoder(
        "bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder())
    )

    @Bean
    fun authProvider(customUDS: CustomUserDetailService): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(customUDS)
            setPasswordEncoder(delegatingPasswordEncoder())
        }
    }
}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class GlobalMethodSecurityConfig : GlobalMethodSecurityConfiguration()