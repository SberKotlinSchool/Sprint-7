package com.example.demo.service

//import com.example.demo.repository.UserRepository
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.core.userdetails.UsernameNotFoundException
//import org.springframework.stereotype.Service
//
//@Service
//class UserService(val userRepository: UserRepository): UserDetailsService {
//    override fun loadUserByUsername(username: String?): UserDetails =
//       userRepository.findByUsername(username!!)
//           ?: throw UsernameNotFoundException(/* msg = */ "User is not found")
//}