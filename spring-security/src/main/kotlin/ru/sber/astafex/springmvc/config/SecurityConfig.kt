package ru.sber.astafex.springmvc.config

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
import ru.sber.astafex.springmvc.service.CustomUserDetailService

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http {
            authorizeRequests {
                authorize("/rest/**", "hasRole('ROLE_ADMIN') or hasRole('ROLE_API')")
                authorize("/app/**", "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
                authorize("/**", authenticated)
            }

            formLogin { permitAll() }

            csrf { ignoringAntMatchers("/h2-console/*") }

            headers {
                frameOptions { sameOrigin = true }
            }
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