package ru.sber.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
internal class SecurityConfiguration {

  @Bean
  @Profile("test-api")
  @Throws(Exception::class)
  fun filterChainBasic(http: HttpSecurity): SecurityFilterChain {
    http.csrf().disable().httpBasic()
      .and()
      .authorizeRequests()
      .antMatchers("/api/**").hasAnyRole("API", "ADMIN")
      .anyRequest().permitAll()

    return http.build()
  }

  @Bean
  @Profile("default")
  @Throws(Exception::class)
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf().ignoringRequestMatchers(AntPathRequestMatcher("/h2/console/**"))
      .and()
      .formLogin().permitAll()
      .and()
      .authorizeRequests()
      .antMatchers("/api/**").hasAnyRole("API", "ADMIN")
      .antMatchers("/app/**").hasAnyRole("APP", "ADMIN")
      .antMatchers("/h2/console/**").hasRole("ADMIN")
      .antMatchers("/**").authenticated()
      .and().headers().frameOptions().sameOrigin()

    return http.build()
  }

}
