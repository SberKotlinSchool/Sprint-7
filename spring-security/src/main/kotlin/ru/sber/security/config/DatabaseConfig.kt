package ru.sber.security.config

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

    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build()
    }

    @Bean
    fun users(dataSource: DataSource): UserDetailsManager {

        val users = JdbcUserDetailsManager(dataSource)

        val userDetailsList: List<UserDetails> = listOf(
            createUser("USER_APP", "Qq123456", "APP"),
            createUser("USER_API", "Qq123456", "API"),
            createUser("ADMIN", "Qq123456", "ADMIN")
        )
        userDetailsList.forEach { users.createUser(it) }
        return users
    }

    private fun createUser(username: String, password: String, vararg roles: String): UserDetails {
        return User.builder()
            .username(username)
            .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
            .roles(*roles)
            .build()
    }
}
