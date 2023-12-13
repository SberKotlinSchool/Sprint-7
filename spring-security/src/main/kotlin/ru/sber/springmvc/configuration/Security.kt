package ru.sber.springmvc.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class Security {
    @Bean
    @Profile("default")
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/api/**", "hasRole('ROLE_API') or hasRole('ROLE_ADMIN')")
                authorize("/app/**", "hasRole('ROLE_APP') or hasRole('ROLE_ADMIN')")
                authorize(anyRequest, denyAll)
            }
            formLogin {
                permitAll()
            }
            csrf {
                ignoringAntMatchers("/api/**")
            }
            httpBasic { }
        }
        return http.build()
    }

    @Bean
    @Profile("test")
    fun filterChainTest(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/api/**", "hasRole('ROLE_API') or hasRole('ROLE_ADMIN')")
                authorize("/app/**", "hasRole('ROLE_APP') or hasRole('ROLE_ADMIN')")
                authorize(anyRequest, denyAll)
            }
            formLogin {
                permitAll()
            }
            csrf {
                disable()
            }
            httpBasic { }
        }
        return http.build()
    }
}