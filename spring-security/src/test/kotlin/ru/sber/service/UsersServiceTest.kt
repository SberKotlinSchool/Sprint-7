package ru.sber.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import ru.sber.model.Role
import ru.sber.model.UserData
import ru.sber.repository.UserRepository
import kotlin.test.assertTrue

@SpringBootTest
internal class UsersServiceTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var usersService: UsersService

    @Test
    fun test() {
        userRepository.save(
            UserData(
                login = "api",
                password = "\$2a\$10\$6k8ogL41MOwrSnpvMQ9C9.lzu614qsDvMjPKb0H85vGnM/rSJwN76",
                roles = mutableListOf(Role(roles = "API"))
            )
        )
        println(
            userRepository.save(
                UserData(
                    login = "admin",
                    password = "\$2a\$10\$HAOqnZ3W3YHXjik2OxvyGOWVIP5IAfFAibm8JT3FhQrhyPM8puS1e",
                    roles = mutableListOf(Role(roles = "ADMIN"))
                )
            )
        )
        userRepository.save(
            UserData(
                login = "user",
                password = "\$2a\$10\$zF5bIwkaf2IAfMjqMqAHPOdnRpykD2dCeipBzP2Oq1GbdLaDJsq7e",
                roles = mutableListOf(Role(roles = "USER"))
            )
        )

        var userDetails = usersService.loadUserByUsername("user")
        assertTrue(BCryptPasswordEncoder().matches("user", userDetails.password))

        userDetails = usersService.loadUserByUsername("admin")
        assertTrue(BCryptPasswordEncoder().matches("admin", userDetails.password))

        userDetails = usersService.loadUserByUsername("api")
        assertTrue(BCryptPasswordEncoder().matches("api", userDetails.password))

    }
}