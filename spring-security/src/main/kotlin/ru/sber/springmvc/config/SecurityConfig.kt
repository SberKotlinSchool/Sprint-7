package ru.sber.springmvc.config

import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(method = HttpMethod.DELETE, "/**", "hasRole('ADMIN')")
                authorize("/api/**", "hasRole('API') or hasRole('ADMIN')")
                authorize("/app/**", "hasRole('APP') or hasRole('ADMIN')")
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