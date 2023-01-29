package ru.sber.astafex.springmvc.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sber.astafex.springmvc.filter.LogFilter

@Configuration
class ApplicationConfig {
    @Bean
    fun logger(): Logger = LoggerFactory.getLogger(LogFilter::class.java)
}