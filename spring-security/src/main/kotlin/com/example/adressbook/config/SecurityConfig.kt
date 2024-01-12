package com.example.adressbook.config

import com.example.adressbook.persistence.entity.UserAuthority
import com.example.adressbook.persistence.entity.UserRole
import com.example.adressbook.service.CustomUserDetailsService
import org.h2.engine.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/login*", permitAll)
                authorize("/reg", permitAll)
                authorize("/app/**", authenticated)
                authorize("/api/**", hasAuthority(UserAuthority.API_ACCESS.authority))
                authorize("/app/**", hasAuthority(UserAuthority.APP_ACCESS.authority))
                authorize(anyRequest, authenticated)
            }
            csrf { disable() }
//            httpBasic {  }
            formLogin {
                loginPage="/login"
                failureUrl="/login?error"
                defaultSuccessUrl("/app/list", true)
            }
        }
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider() =
        DaoAuthenticationProvider(passwordEncoder()).apply {
            setUserDetailsService(customUserDetailsService)
        }
}