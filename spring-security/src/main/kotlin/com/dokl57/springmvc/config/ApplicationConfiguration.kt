package com.dokl57.springmvc.config

import com.dokl57.springmvc.filter.LoggingFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("com.dokl57")
class ApplicationConfiguration {
    @Bean
    fun logger(): Logger = LoggerFactory.getLogger(LoggingFilter::class.java)
}