package ru.sber.config

import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.sber.service.LoginService

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(
    val loginService: LoginService
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }


    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(loginService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/app/**").hasRole("ADMIN")
            .antMatchers("/api/**").hasAnyRole("ADMIN", "API")
            .antMatchers("/**").hasAnyRole("ADMIN", "USER")
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
    }
}