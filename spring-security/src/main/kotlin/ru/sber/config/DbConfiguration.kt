package ru.sber.config

import javax.sql.DataSource
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


@Configuration
class DbConfiguration {

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
    val admin: UserDetails = userBuilder
      .username("admin")
      .password(encoder.encode("admin"))
      .roles("ADMIN")
      .build()
    val userApp: UserDetails = userBuilder
      .username("app")
      .password(encoder.encode("app"))
      .roles("APP")
      .build()
    val userApi: UserDetails = userBuilder
      .username("api")
      .password(encoder.encode("api"))
      .roles("API")
      .build()

    val users = JdbcUserDetailsManager(dataSource)
    users.createUser(userApi)
    users.createUser(userApp)
    users.createUser(admin)
    return users
  }

}