package ru.sber.addressbook.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import ru.sber.addressbook.dto.LoginDto
import ru.sber.addressbook.service.UserService

@Controller
class LoginController(
    private val userDetailsService: UserService
) {

  @GetMapping("/login")
  fun login(): String {
    return "login"
  }

  @PostMapping("/signin")
  fun login(loginDto: LoginDto): String {
    userDetailsService.loginUser(loginDto)
    return "redirect:/app/list"
  }
}