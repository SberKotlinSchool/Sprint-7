package ru.sber.springmvc.config

import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ServletComponentScan("ru.sber.springmvc.auth", "ru.sber.springmvc.filter")
class AppConfig