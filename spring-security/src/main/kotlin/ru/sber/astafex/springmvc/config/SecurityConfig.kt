package ru.sber.astafex.springmvc.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke


@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http {
            authorizeRequests {
                authorize("/app/**", authenticated)
                authorize("/api/**", "hasRole('ADMIN') or hasRole('APP')")
                authorize("/public/**", permitAll)
                authorize("/customLoginForm", anonymous)
                authorize(anyRequest, denyAll)
            }
            formLogin {
                loginPage = "/customLoginForm"
            }
        }
    }
}