package com.sbuniver.homework.config

import com.sbuniver.homework.service.OwnUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource


@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var userDetailsService: OwnUserDetailsService

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/h2/*").permitAll()
            .antMatchers("/h2").permitAll()
            .antMatchers("/app/*").hasAnyRole("ADMIN", "USER")
            .antMatchers("/api/*/delete").hasAnyRole("ADMIN")
            .antMatchers("/api/*").hasAnyRole("ADMIN", "APIUSER")
            .antMatchers("/**").denyAll()
            .and().formLogin()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication().dataSource(dataSource)
    }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()


    @Bean
    fun authProvider() = DaoAuthenticationProvider().apply {
        this.setUserDetailsService(userDetailsService)
        this.setPasswordEncoder(getPasswordEncoder())
    }
}
