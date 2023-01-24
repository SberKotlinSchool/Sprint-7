package ru.sber.addressbook


import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ComponentScan("ru.sber.addressbook")
@ServletComponentScan("ru.sber.addressbook.filters")
class SpringConfig : WebMvcConfigurer {

}