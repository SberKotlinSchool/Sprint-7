package ru.sber.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sber.filter.RequestLogFilter


@Configuration
class ApplicationConfig {
    @Bean
    fun logger(): Logger = LoggerFactory.getLogger(RequestLogFilter::class.java)
}