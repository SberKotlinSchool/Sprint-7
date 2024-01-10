package ru.sber.security.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sber.security.filter.LoggerFilter

@Configuration
class LoggerConfig {
    @Bean
    fun logger(): Logger {
        return LoggerFactory.getLogger(LoggerFilter::class.java)
    }
}