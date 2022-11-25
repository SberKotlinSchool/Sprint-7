package ru.sber.spring.mvc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import ru.sber.spring.mvc.service.CustomUserDetailService


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

//    @Bean
//    fun dataSource(): DataSource {
//        return EmbeddedDatabaseBuilder()
//            .setType(EmbeddedDatabaseType.H2)
//            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//            .build()
//    }
//
//    @Bean
//    fun users(dataSource: DataSource, passwordEncoder: PasswordEncoder): UserDetailsManager? {
//        val users = JdbcUserDetailsManager(dataSource)
//        getUsers(passwordEncoder).forEach(users::createUser)
//        return users
//    }

//    private fun getUsers(passwordEncoder: PasswordEncoder): List<UserDetails> {
//        return listOf(
//            User
//                .withUsername("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles("ADMIN")
//                .build(),
//            User
//                .withUsername("user")
//                .password(passwordEncoder.encode("user"))
//                .roles("USER")
//                .build()
//        )
//    }

    @Bean
    fun authProvider(detailService: CustomUserDetailService): AuthenticationProvider {
        val manager = DaoAuthenticationProvider()
        manager.setUserDetailsService(detailService) //подключаем нашу реализацию UserDetailsService
        manager.setPasswordEncoder(passwordEncoder()) //подключаем наш настроенный DelegatingPasswordEncoder
        return manager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {

        http
            .authorizeHttpRequests()
            .antMatchers("/book**", "/rest/book**")
            .authenticated()
            .and()
            .formLogin {
                it.loginProcessingUrl("/login")
            }

        http
            .csrf()
            .ignoringAntMatchers("/h2-console/*")

        http
            .headers()
            .frameOptions()
            .sameOrigin()

        return http.build()
    }
}