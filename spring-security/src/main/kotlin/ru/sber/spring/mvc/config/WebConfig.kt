package ru.sber.spring.mvc.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.sber.spring.mvc.filter.LoggerFilter


@Configuration
@ComponentScan
class WebConfig {

    @Bean
    fun loggerFilter(): FilterRegistrationBean<LoggerFilter>? {
        val regBean = FilterRegistrationBean<LoggerFilter>()
        regBean.order = 1
        regBean.filter = LoggerFilter()
        return regBean
    }
}
