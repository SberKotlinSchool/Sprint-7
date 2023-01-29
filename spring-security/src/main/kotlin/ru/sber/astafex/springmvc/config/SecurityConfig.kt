package ru.sber.astafex.springmvc.config

import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
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
//                authorize("/**", authenticated)
//                authorize("/api/**", "hasRole('ADMIN') or hasRole('API')")
                authorize(anyRequest, permitAll)
            }
            formLogin { }


            csrf {
                //отключаем csrf для h2-console
                ignoringAntMatchers("/h2-console/*")
            }
            headers {
                frameOptions {
                    //разрешаем работу в iframe внутри домена для h2-console
                    sameOrigin = true
                }
            }
        }
    }

    /**
     * Настраиваем DelegatingPasswordEncoder для поддержки нескольких типов хранимых паролей
     */
    @Bean
    fun delegatingPasswordEncoder(): DelegatingPasswordEncoder = DelegatingPasswordEncoder(
        "bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder())
    )

    /**
     * Настраиваем наш DaoAuthenticationProvider
     */
    @Bean
    fun authProvider(customUDS: CustomUserDetailService): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(customUDS)
            setPasswordEncoder(delegatingPasswordEncoder())
        }
    }
}