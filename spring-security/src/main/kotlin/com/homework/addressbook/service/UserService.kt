package com.homework.addressbook.service;

import com.homework.addressbook.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserService @Autowired constructor(private val userRepository: UserRepository): UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findUserByLogin(username)?: throw UsernameNotFoundException("User with username $username not found")
    }

}
