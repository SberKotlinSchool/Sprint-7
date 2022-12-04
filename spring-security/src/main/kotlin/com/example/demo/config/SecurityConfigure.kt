package com.example.demo.config

import com.example.demo.service.CustomUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder


@EnableWebSecurity
class SecurityConfigure : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var myProvider: AuthenticationProvider


    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/**").hasAnyRole("ADMIN", "API")
            .antMatchers("/**").hasAnyRole("ADMIN", "API", "USER")
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(myProvider)
    }

    @Bean
    fun delegatingPasswordEncoder(): DelegatingPasswordEncoder = DelegatingPasswordEncoder(
        "bcrypt", mapOf(
            "bcrypt" to BCryptPasswordEncoder()
        )
    )

    @Bean
    fun authProvider(
        customUDS: CustomUserDetailService,
        delegatingPasswordEncoder: DelegatingPasswordEncoder
    ): AuthenticationProvider {
        val manager = DaoAuthenticationProvider()
        manager.setUserDetailsService(customUDS)
        return manager
    }


}