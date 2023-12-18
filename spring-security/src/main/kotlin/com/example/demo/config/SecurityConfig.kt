package com.example.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
//    private val userService: UserService
) {
//    @Bean
//    fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            authorizeRequests {
//                authorize("/api/**", "hasRole('API') or hasRole('ADMIN')")
//                authorize("/app/**", authenticated)
//                authorize("/h2-console**", permitAll)
                authorize(anyRequest, permitAll)
            }
            formLogin {
                permitAll()
            }

            csrf { disable() }
            httpBasic {  }
        }
        return http.build()
    }

//    @Primary
//    fun configureAuthentication(auth: AuthenticationManagerBuilder): AuthenticationManagerBuilder {
//        return auth.authenticationProvider(authenticationProvider())
//    }
//
//    @Bean
//    fun authenticationProvider(): DaoAuthenticationProvider {
//        val authProvider = DaoAuthenticationProvider()
//        authProvider.setUserDetailsService(userService)
//        authProvider.setPasswordEncoder(passwordEncoder())
//        return authProvider
//    }

    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build()
    }

    @Bean
    fun users(dataSource: DataSource): UserDetailsManager {
        val userBuilder = User.withDefaultPasswordEncoder()
        val userApp: UserDetails = userBuilder
            .username("app")
            .password("app")
            .roles("APP")
            .build()
        val userApi: UserDetails = userBuilder
            .username("api")
            .password("api")
            .roles("API")
            .build()
        val admin: UserDetails = userBuilder
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build()

        val users = JdbcUserDetailsManager(dataSource)
        users.createUser(userApi)
        users.createUser(userApp)
        users.createUser(admin)
        return users
    }
}