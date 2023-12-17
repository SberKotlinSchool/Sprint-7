package ru.sber.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class Security {


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/api/**", "hasRole('API') or hasRole('ADMIN')")
                authorize("/app/**", "hasRole('ADMIN') or hasRole('USER')")
                authorize("/**", authenticated)
            }

            formLogin { permitAll() }

            csrf { disable() }

            httpBasic { }
        }
        return http.build()
    }
}