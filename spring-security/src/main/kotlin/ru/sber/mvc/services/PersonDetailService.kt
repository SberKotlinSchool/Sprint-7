package ru.sber.mvc.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.sber.mvc.repositories.PersonRepository
import mu.KLogging

@Service
class PersonDetailService(private val repository: PersonRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        logger.info("username = $username")
        val person = repository.findByUsername(username) ?: throw UsernameNotFoundException("User not found!")
        logger.info("password = ${person.password}")
        return person.toPersonDetails()
    }
    companion object : KLogging()
}