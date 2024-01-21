package com.firebat.addressbook.config

import com.firebat.addressbook.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var authenticationProvider: AuthenticationProvider

    override fun configure(http: HttpSecurity?) {
        http {
            authorizeRequests {
                authorize("/app/**", authenticated)
                authorize("/api/**", hasAnyRole("ROLE_ADMIN", "ROLE_API", "ROLE_API_DELETE"))
                authorize("/login", anonymous)
                authorize("/h2-console/**", anonymous)
                authorize(anyRequest, denyAll)
            }
            formLogin {
                loginPage = "/login"
                defaultSuccessUrl("/app/list", true)
                permitAll()
            }
            headers {
                frameOptions {
                    sameOrigin = true
                }
            }
            csrf {
                ignoringAntMatchers("/h2-console/*")
            }
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(authenticationProvider)
    }

    @Bean
    fun delegatingPasswordEncoder(): DelegatingPasswordEncoder = DelegatingPasswordEncoder(
        "bcrypt", mapOf(
            "bcrypt" to BCryptPasswordEncoder()
        )
    )

    @Bean
    fun authProvider(
        authService: AuthService,
        delegatingPasswordEncoder: DelegatingPasswordEncoder
    ): AuthenticationProvider {
        val manager = DaoAuthenticationProvider()
        manager.setUserDetailsService(authService)
        manager.setPasswordEncoder(delegatingPasswordEncoder)
        return manager
    }
}