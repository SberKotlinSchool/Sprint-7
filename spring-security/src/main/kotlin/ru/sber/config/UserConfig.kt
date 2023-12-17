package ru.sber.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import javax.sql.DataSource

@Configuration
class UserConfig {

    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build()
    }

    @Bean
    fun users(dataSource: DataSource): UserDetailsManager {
        val userBuilder = User.withDefaultPasswordEncoder()
        val users = JdbcUserDetailsManager(dataSource)

        users.createUser(
            userBuilder
                .username("api")
                .password("api")
                .roles("API")
                .build()
        )
        users.createUser(
            userBuilder
                .username("user")
                .password("user")
                .roles("USER")
                .build()
        )
        users.createUser(
            userBuilder
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build()
        )
        return users
    }
}