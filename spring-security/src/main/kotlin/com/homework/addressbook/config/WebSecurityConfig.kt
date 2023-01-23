package com.homework.addressbook.config

import com.homework.addressbook.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig @Autowired constructor(): WebSecurityConfigurerAdapter()
{
    @Autowired
    private val userService: UserService? = null

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .csrf()
            .disable()
            .authorizeRequests()
            //Доступ только для не зарегистрированных пользователей
            .antMatchers("/registration").not().fullyAuthenticated()
            //Доступ только для пользователей с ролью API
            .antMatchers("/api/**").hasRole("API")
            //Доступ только для пользователей с ролью USER|ADMIN|EDITOR
            .antMatchers("/app/").access("hasRole('USER') or hasRole('ADMIN') or hasRole('EDITOR')")
            //Доступ разрешен всем пользователей
            .antMatchers("/",).permitAll()
            //Все остальные страницы требуют аутентификации
            .anyRequest().authenticated()
            .and()
            //Настройка для входа в систему
            .formLogin()
            .loginPage("/login")
            //Перенарпавление на главную страницу после успешного входа
            .defaultSuccessUrl("/app/list")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .logoutSuccessUrl("/");
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService?>(userService)
            .passwordEncoder(bCryptPasswordEncoder())
    }
}