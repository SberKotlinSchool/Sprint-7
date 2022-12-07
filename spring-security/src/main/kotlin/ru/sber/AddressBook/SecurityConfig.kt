package ru.sber.AddressBook

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
import ru.sber.AddressBook.Services.CustomUserDetailService

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/app/**", authenticated)
                authorize("/api/**", "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER_API')")
            }
            formLogin {
                permitAll()
                defaultSuccessUrl("/app/list", true)
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
    fun authProvider(customerDetailService: CustomUserDetailService): AuthenticationProvider {
        val manager = DaoAuthenticationProvider()
        manager.setUserDetailsService(customerDetailService)
        manager.setPasswordEncoder(passwordEncoder())
        return manager
    }
}