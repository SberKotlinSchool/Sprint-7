package com.example.adressbook.controller

import com.example.adressbook.dto.UserCreateDto
import com.example.adressbook.persistence.entity.UserRole
import com.example.adressbook.service.CustomUserDetailsService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class LoginController(
    private val userDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/login")
    fun login() = "login"

    @PostMapping("/login")
    fun authorize(): String {
        return "redirect:app/list"
    }

    @GetMapping("/reg")
    fun register(model: Model): String {
        model.addAttribute("user", UserCreateDto())
        return "reg"
    }

    @PostMapping("/reg")
    fun createNewUser(
        @ModelAttribute user: UserCreateDto
    ): String {
        userDetailsService.createUserIfPossible(
            user.apply {
                password = passwordEncoder.encode(password)
                if (role == null)
                    role = UserRole.ROLE_APP
            }
        )
        return "redirect:app/list"
    }
}