package org.example.springmvc.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import javax.sql.DataSource

@Configuration
class DatabaseConfig {
    @Value("\${admin.username}")
    private val admin: String = ""

    @Value("\${admin.password}")
    private val password: String = ""

    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build()
    }

    @Bean
    fun users(dataSource: DataSource): UserDetailsManager {
        val userBuilder = User.builder()
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val userApp: UserDetails = userBuilder
            .username("USER_APP")
            .password(encoder.encode("App123456"))
            .roles("APP")
            .build()
        val userApi: UserDetails = userBuilder
            .username("USER_API")
            .password(encoder.encode("Api123456"))
            .roles("API")
            .build()
        val admin: UserDetails = userBuilder
            .username(admin)
            .password(encoder.encode(password))
            .roles("ADMIN")
            .build()
        val users = JdbcUserDetailsManager(dataSource)
        users.createUser(userApi)
        users.createUser(userApp)
        users.createUser(admin)
        return users
    }
}