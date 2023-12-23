package com.example.adresbook.service

import com.example.adresbook.dto.LoginDto
import com.example.adresbook.dto.LogonDto
import com.example.adresbook.model.ApplicationUser
import com.example.adresbook.model.UserRole
import com.example.adresbook.repository.RoleRepository
import com.example.adresbook.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val customUserDetailsService: CustomUserDetailsService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    fun loginUser(loginDto: LoginDto): ApplicationUser {
        val user = userRepository.findByUserName(loginDto.username)
        if (user == null) {
            throw Exception("Пользователь сущестсует")
        } else if (user.password == loginDto.password) {
            customUserDetailsService.loadUserByUsername(user.userName)
        }
        return user
    }

    fun logonUser(logonDto: LogonDto): ApplicationUser {

        if (userRepository.findByUserName(logonDto.username) != null) {
            throw Exception("Пользователь сущестсует")
        }

        val appUser: ApplicationUser = logonDto.toApplicationUser()


        val roles: Set<UserRole> = roleRepository.findAllByName(logonDto.role)
        appUser.roles = roles

        return userRepository.save(appUser)
    }

}