package ru.sber.mvc.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.sber.mvc.services.PersonDetailService

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(private val personService: PersonDetailService) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(personService).passwordEncoder(getPasswordEncoder())
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.authorizeRequests()
            .antMatchers("/app/**").hasAnyRole("ADMIN", "READER", "WRITER")
            .antMatchers("/api/**").hasAnyRole("ADMIN", "API")
            .antMatchers("/login", "/error").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .loginProcessingUrl("/process_login")
            .defaultSuccessUrl("/app/list", true)
            .failureUrl("/login?error")
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
    }

}