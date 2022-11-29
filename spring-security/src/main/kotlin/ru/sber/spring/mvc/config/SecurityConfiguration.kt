package ru.sber.spring.mvc.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import ru.sber.spring.mvc.service.CustomUserDetailService


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
class SecurityConfiguration {

    @Autowired
    private lateinit var env: Environment

    fun isIntegrationTest():Boolean = env.activeProfiles.any { "integration-test".equals(it) }

    @Bean
    fun authProvider(detailService: CustomUserDetailService): AuthenticationProvider {
        val manager = DaoAuthenticationProvider()
        manager.setUserDetailsService(detailService) //подключаем нашу реализацию UserDetailsService
        manager.setPasswordEncoder(passwordEncoder()) //подключаем наш настроенный DelegatingPasswordEncoder
        return manager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {

        if (isIntegrationTest()) {
            http
                .csrf()
                .disable()
        } else {
            http
                .csrf()
                .ignoringAntMatchers("/h2/*")
        }

        http
            .headers()
            .frameOptions()
            .sameOrigin()
            .disable()

        http
            .authorizeHttpRequests()
            .antMatchers("/book/**")
            .hasAnyAuthority("ROLE_ADMIN","ROLE_READ","ROLE_WRITE")
            .and()
            .authorizeHttpRequests()
            .antMatchers("/rest/book**")
            .hasAuthority("ROLE_USER")
            //.authenticated()
            .and()
            .formLogin {
                it.loginProcessingUrl("/login")
            }
            .logout()

        return http.build()
    }

}