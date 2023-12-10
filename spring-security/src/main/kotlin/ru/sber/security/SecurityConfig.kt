package ru.sber.security

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
import ru.sber.persistence.AuthorityGroup


@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http {
            authorizeRequests {
                authorize("/api/*/delete", "hasRole('${AuthorityGroup.ADMIN}')")
                authorize("/app/*/delete", "hasRole('${AuthorityGroup.ADMIN}')")
                authorize("/api/**", "hasRole('${AuthorityGroup.ADMIN}') or hasRole('${AuthorityGroup.API}')")
                authorize("/app/**", "hasRole('${AuthorityGroup.ADMIN}') or hasRole('${AuthorityGroup.USER}') ")
                authorize("/login", anonymous)
                authorize(anyRequest, denyAll)
            }

            formLogin {
                loginPage = "/login"
            }
            csrf { disable() }
            headers { frameOptions { sameOrigin = true } }
        }
    }

    @Bean
    fun delegatingPasswordEncoder(): DelegatingPasswordEncoder = DelegatingPasswordEncoder(
            "bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder())
    )

    @Bean
    fun authProvider(service: UserDetailSecurityService): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(service)
            setPasswordEncoder(delegatingPasswordEncoder())
        }
    }
}

//Acl
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class GlobalMethodSecurityConfig : GlobalMethodSecurityConfiguration()
