package ru.sber.springsec.config

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecConf {
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

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/api/**", "hasRole('API') or hasRole('ADMIN')")
                authorize("/app/**", authenticated)
                authorize(anyRequest, denyAll)
            }
            formLogin {
                permitAll()
            }
            csrf { disable() }
            httpBasic {  }
        }
        return http.build()
    }

}