package com.example.springsecurity.config

import com.example.springsecurity.service.CustomUserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/**", authenticated)
                authorize("/api/**", "hasRole('ADMIN') or hasRole('API')")
            }
            formLogin {
                permitAll()
                defaultSuccessUrl("/mainpage", true)
            }
            csrf {
                disable()
                ignoringAntMatchers("/h2-console/*")
            }
            headers {
                frameOptions {
                    sameOrigin = true
                }
            }
        }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authProvider(userService: CustomUserDetailService): AuthenticationProvider {
        val manager = DaoAuthenticationProvider()
        manager.setUserDetailsService(userService)
        manager.setPasswordEncoder(passwordEncoder())
        return manager
    }
}