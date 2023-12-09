package ru.sber.spring.boot.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class Controller {
  @GetMapping("spring")
  fun start() = "hello"
}