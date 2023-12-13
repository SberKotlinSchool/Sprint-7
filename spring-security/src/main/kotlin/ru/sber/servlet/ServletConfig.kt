package ru.sber.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sber.servlet.filter.LogFilter

@Configuration
class ServletConfig {

    /**
     * Фильтр логирования входящих http-запросов
     */
    @Bean
    fun logFilter(): FilterRegistrationBean<*>? {
        val registrationBean = FilterRegistrationBean<LogFilter>()
        registrationBean.filter = LogFilter()
        registrationBean.addUrlPatterns("/app/*", "/api/*")
        registrationBean.order = 1
        return registrationBean
    }
}

