package ru.sber.addressbook.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import ru.sber.addressbook.dto.LogonDto
import ru.sber.addressbook.service.UserService

@Controller
class LogonController(
    private val userDetailsService: UserService
) {

  @GetMapping("/logon")
  fun login(): String {
    return "logon"
  }

  @PostMapping("/logon")
  fun login(logonDto: LogonDto): String {
    println(logonDto)
    userDetailsService.logonUser(logonDto)
    return "redirect:/app/list"
  }
}